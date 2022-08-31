package com.example.hsproject


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.hsproject.api.APIList
import com.example.hsproject.databinding.ActivityInvitedAppointDetailBinding
import com.example.hsproject.datas.InvitedAppointmentsData
import com.example.hsproject.datas.odsaydatas.ODSayResponse
import com.example.hsproject.utils.ContextUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.PathOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.ArrayList

class InvitedAppointDetailActivity : BaseActivity() {
    lateinit var binding : ActivityInvitedAppointDetailBinding
    lateinit var naverMap: NaverMap

    lateinit var invitedAppointmentData : InvitedAppointmentsData

    lateinit var startLatLng : LatLng
    lateinit var endLatLng : LatLng

    var listLatLng = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invited_appoint_detail)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        val token = ContextUtil.getLoginToken(mContext)

        //대화창 //약속리사티클러어답터에서 인텐트로 받은 정보를 대화창을 위해 한번 더 인텐트로 보낸다

        messageIcon.setOnClickListener {
            val myIntent = Intent(mContext, InvitedChattingActivity::class.java)
            myIntent.putExtra("invitedAppointmentData", invitedAppointmentData)
            mContext.startActivity(myIntent)
        }

    }










    override fun setValues() {

        val formatter = SimpleDateFormat("M/dd a h:ss")
        //전 페이지에서 하나의 약속정보 가져옴
        invitedAppointmentData = intent.getSerializableExtra("invitedAppointmentData") as InvitedAppointmentsData

        startLatLng = LatLng(invitedAppointmentData.startLatitude, invitedAppointmentData.startLongitude)
        endLatLng = LatLng(invitedAppointmentData.latitude, invitedAppointmentData.longitude)


        binding.titleTxt.text = invitedAppointmentData.title
        binding.dateTxt.text = formatter.format(invitedAppointmentData.datetime)
        backIcon.visibility = View.VISIBLE
        messageIcon.visibility = View.VISIBLE

        var friend = ""
        if (invitedAppointmentData.invitedFriends != null) {
            for (UserData in invitedAppointmentData.invitedFriends) {
                friend += UserData.nickname.toString() + ", "
            }
            friend = friend.substring(0, friend.length - 2)

        }

        binding.friendTxt.text =
            "인원 : ${invitedAppointmentData.invitedFriends.size.toString()}명 (${friend})"


        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //@@@@@@@@@@@@@@@@@@@@@@@@@@지도객체@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        //네이버 맵 객체화
        mapFragment.getMapAsync {
            //지도 로딩이 끝난후 얻어낸 온전한 지도 객체 변수화
            naverMap = it

            //val coord = LatLng(listLatLng[0].latitude,listLatLng[0].longitude)
            val coord = LatLng(invitedAppointmentData.latitude, invitedAppointmentData.longitude)
            val cameraPosition = CameraPosition(coord, 12.0)
            //처음 시작 위치 보여주기
            val cameraUpdate = CameraUpdate.scrollTo(coord)
            //naverMap.moveCamera(cameraUpdate)
            naverMap.cameraPosition = cameraPosition

            findWay()
        }





    }
    fun findWay() {
        val apiKey = "qMKUx9YrEQdTXRPwh4Ot9PEoBMfWy4oDjSsR4PHjCq4"
        //서버에서 받아온 정보를 이미 lateinit var 로 만듬? 그래서 아마 바로 사용해도 될듯
        /*val startLatLng = LatLng(37.6134436427887, 126.926493082645)
        val endLatLng = LatLng(37.5004198786564, 127.126936754911)*/
        val odsayRetrofit = Retrofit.Builder()
            .baseUrl("https://api.odsay.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = odsayRetrofit.create(APIList::class.java)
        api.getRequestFindWay(
            apiKey,
            startLatLng.longitude,
            startLatLng.latitude,
            endLatLng.longitude,
            endLatLng.latitude
        ).enqueue(object : Callback<ODSayResponse> {
            override fun onResponse(call: Call<ODSayResponse>, response: Response<ODSayResponse>) {
                if (response.isSuccessful) {
                    var br = response.body()!!

                    if(br.result != null){
                        val result = br.result!!

                        listLatLng.clear()
                        listLatLng.add(LatLng(startLatLng.latitude,startLatLng.longitude))
                        for (num in 1 until result.path[0].subPath.size step 2) {
                            /*Log.d("문제 숫자", "${num}")*/
                            listLatLng!!.add(
                                LatLng(
                                    result.path[0].subPath[num].startY,
                                    result.path[0].subPath[num].startX
                                )
                            )
                            //Log.d("문제 경로","${num} ${br.result.path[0].subPath[num].startY} ${br.result.path[0].subPath[num].startX}")
                        }
                        listLatLng.add(LatLng(endLatLng.latitude,endLatLng.longitude))

                        //오디세이에서 정보를 받아올 경우에만 경로를 그려주기
                        val path = PathOverlay()
                        //경로 그리기
                        path.coords = listLatLng//@@@@@@@@@@@@@
                        path.map = naverMap

                        when(result.path[0].pathType){
                            1 -> binding.pathTypeTxt.text = "지하철"
                            2 -> binding.pathTypeTxt.text = "버스"
                            else -> binding.pathTypeTxt.text = "지하철 + 버스"
                        }
                        binding.totalTimeTxt.text = "${result.path[0].info.totalTime} 분"
                        binding.paymentTxt.text = "(${result.path[0].info.payment}원)"
                        binding.totalDistanceTxt.text = "(${result.path[0].info.getTotalDistance()})"
                        binding.firstStartStationTxt.text ="${result.path[0].info.firstStartStation}"
                        binding.lastEndStationTxt.text = "${result.path[0].info.lastEndStation}"
                        binding.lastEndStation2Txt.text = "${result.path[0].info.lastEndStation}"

                    }




                    //여기서 정보를 다 저장할거다 레잇이닛 바 로
                    /*Log.d("응답", response.body().toString())
                    Log.d("문제 지하철 버스 버스+지하철", "${br.result.path[0].pathType}")//1지하철, 2버스, 3버스+지하철
                    Log.d("문제 첫번째 경로", "${br.result.path[0]}")//안에 배열인데 나는 첫번쨰꺼 하나만 사용
                    Log.d("문제 첫번째 경로 총 소요시간", "${br.result.path[0].info.totalTime}")//첫번째 경로의 총 소요시간
                    Log.d("문제 총 금액", "${br.result.path[0].info.payment}")//첫번째 경로의 총 금액
                    Log.d("문제 총 거리", "${br.result.path[0].info.totalDistance}")//첫번째 경로의 이동거리
                    Log.d("문제 출발지", "${br.result.path[0].info.firstStartStation}")//첫번째 경로의 출발지
                    Log.d("문제 도착지", "${br.result.path[0].info.lastEndStation}")//첫번째 경로의 도착지*/



                    /*Log.d("문제 경로들", "${br.result.path[0].subPath}")//첫번째 경로의 환승정보를 담은 리스트*/
                    /*Log.d("문제 ListLatLng", "${br.result.path[0].subPath.size}")*/
                    /*Log.d("문제 경로", "${listLatLng}")*/
                    /*
                    Log.d("문제 경로들의 정보", "${br.result.path[0].subPath[1]}")//첫번째 경로의 환승정보를 담은 리스트
                    Log.d("문제 경로들의 정보", "${br.result.path[0].subPath[1].startX}")//첫번째 경로의 환승정보를 담은 리스트
                    Log.d("문제 경로들의 정보", "${br.result.path[0].subPath[1].startY}")//첫번째 경로의 환승정보를 담은 리스트*/

                }
            }

            override fun onFailure(call: Call<ODSayResponse>, t: Throwable) {

            }
        })
    }
}