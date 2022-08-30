package com.example.hsproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.api.APIList
import com.example.hsproject.databinding.ActivityAppointDetailBinding
import com.example.hsproject.datas.AppointmentData
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.odsaydatas.ODSayResponse
import com.example.hsproject.fragments.MyAppointmentFragment
import com.example.hsproject.utils.ContextUtil
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.PathOverlay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat

class AppointDetailActivity : BaseActivity() {

    lateinit var binding: ActivityAppointDetailBinding
    lateinit var mapView: MapView

    lateinit var appointmentData: AppointmentData

    lateinit var startLatLng : LatLng
    lateinit var endLatLng : LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appoint_detail)
        setValues()
        setupEvents()
    }

    override fun onResume() {
        super.onResume()
        getAppointmentDetailFromServer()
    }

    override fun setupEvents() {

        //약속 수정 //인텐트로 정보 넣어 수정으로 보내주고 => 수정에서 바뀐거 약속 ID추가해서 되돌리기(여기 화면로 새로고침)

        binding.appointmentChangeBtn.setOnClickListener {
            val myIntent = Intent(mContext, ModifyAppointmentActivity::class.java)
            myIntent.putExtra("appointmentData", appointmentData)
            mContext.startActivity(myIntent)
        }

        //토큰 가져오기?????????? 여기에 토큰 코드 써도 되는건가?????????@@@@@@@@@@
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        val token = ContextUtil.getLoginToken(mContext)//@@@@@@@@@@@@@@@@@@@
        //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


        //약속 삭제
        binding.appointmentDeleteBtn.setOnClickListener {
            val alert = AlertDialog.Builder(mContext)
                .setTitle("약속을 삭제하시겠습니까?")
                //확인버튼 선택시
                .setPositiveButton("삭제", DialogInterface.OnClickListener { dialogInterface, i ->
                    apiList.deleteAppointment(appointmentData.id.toString())
                        .enqueue(object : Callback<BasicResponse> {
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                Toast.makeText(mContext, "약속이 삭제되었습니다.", Toast.LENGTH_SHORT)
                                    .show()

                                //약속 삭제했으니깐 이전 프레그먼트 새로고침
                                (mContext as MyAppointmentFragment).getAppointmentDataFromServer()
                                finish()
                            }

                            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                            }
                        })
                })
                .setNegativeButton("취소", null)
                .show()
        }

        //대화창 //약속리사티클러어답터에서 인텐트로 받은 정보를 대화창을 위해 한번 더 인텐트로 보낸다
        //시간여유 생기면 필요한것만 넘기기
        messageIcon.setOnClickListener {
            val myIntent = Intent(mContext, ChattingActivity::class.java)
            myIntent.putExtra("appointmentData", appointmentData)
            mContext.startActivity(myIntent)
        }

    }

    override fun setValues() {
        val formatter = SimpleDateFormat("M/dd a h:ss")
        backIcon.visibility = View.VISIBLE
        messageIcon.visibility = View.VISIBLE

        //전 페이지에서 하나의 약속정보 가져옴
        appointmentData = intent.getSerializableExtra("appointmentData") as AppointmentData
        binding.titleTxt.text = appointmentData.title
        binding.dateTxt.text = formatter.format(appointmentData.datetime)

        getAppointmentDetailFromServer()
        //findWay()

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
            val naverMap = it

            val coord = LatLng(37.5670135, 126.9783740)
            val cameraPosition = CameraPosition(coord, 16.0)
            //처음 시작 위치 보여주기
            val cameraUpdate = CameraUpdate.scrollTo(coord)
            //naverMap.moveCamera(cameraUpdate)
            naverMap.cameraPosition = cameraPosition

/*            val path = PathOverlay()
            path.coords = listOf(
                LatLng(37.57152, 126.97714),
                LatLng(37.56607, 126.98268),
                LatLng(37.56445, 126.97707),
                LatLng(37.55855, 126.97822)
            )
            path.map = naverMap*/


        }

    }


    fun getAppointmentDetailFromServer() {
        apiList.getRequestMyDetailAppointment(appointmentData.id.toString())
            .enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if (response.isSuccessful) {

                        var br = response.body()!!
                        appointmentData = br.data.appointment as AppointmentData
                        //odsay에서 사용할 정보를 넣어준다
                        startLatLng = LatLng(appointmentData.startLatitude, appointmentData.startLongitude)
                        endLatLng = LatLng(appointmentData.latitude, appointmentData.longitude)
                        setUiformData()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })
    }

    fun setUiformData() {
        val formatter = SimpleDateFormat("M/dd a h:ss")
        binding.titleTxt.text = appointmentData.title
        binding.dateTxt.text = formatter.format(appointmentData.datetime)

        var friend = ""
        if (appointmentData.invitedFriends != null) {
            for (UserData in appointmentData.invitedFriends) {
                friend += UserData.nickname.toString() + ", "
            }
            friend = friend.substring(0, friend.length - 2)

        }

        binding.friendTxt.text =
            "인원 : ${appointmentData.invitedFriends.size.toString()}명 (${friend})"
    }


    fun findWay() {
        val apiKey = "qMKUx9YrEQdTXRPwh4Ot9PEoBMfWy4oDjSsR4PHjCq4"
        val startLatLng = LatLng(37.6134436427887, 126.926493082645)
        val endLatLng = LatLng(37.5004198786564, 127.126936754911)
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
                    //여기서 정보를 다 저장할거다 레잇이닛 바 로
                    Log.d("응답", response.body().toString())
                }
            }

            override fun onFailure(call: Call<ODSayResponse>, t: Throwable) {

            }
        })
    }

}