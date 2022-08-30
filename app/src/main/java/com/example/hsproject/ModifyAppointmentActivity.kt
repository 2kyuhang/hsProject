package com.example.hsproject

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.adapters.FriendSpinnerAdapter
import com.example.hsproject.adapters.PlaceSpinnerAdapter
import com.example.hsproject.databinding.ActivityModifyAppointmentBinding
import com.example.hsproject.datas.AppointmentData
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.PlaceData
import com.example.hsproject.datas.UserData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ModifyAppointmentActivity : BaseActivity() {

    lateinit var binding : ActivityModifyAppointmentBinding

    //네이버 지도 좌표
    var mSelectedLatLng: LatLng? = null

    //선택된 약속 일시를 저장할 변수
    val mSelectedDataTime = Calendar.getInstance()//기본값 : 현재 시간

    //출발장소 관련 변수 //스피너 쓸거
    lateinit var mPlaceSpinnerAdapter : PlaceSpinnerAdapter
    val mStartPlaceList = ArrayList<PlaceData>()

    //스피너로 선택된 장소객체를 담고 있는 변수를 만듬
    lateinit var mStartPlace : PlaceData
    //네이버 출발지 지도에 표시하기 위한 변수
    var mNaverMap : NaverMap? = null
    //출발지 마커
    var mStartPlaceMarker = Marker()


    //친구추가 관련 변수
    lateinit var mFriendSpinnerAdapter : FriendSpinnerAdapter
    val mFriendList = ArrayList<UserData>()
    val mSelectedFriendList = ArrayList<UserData>()

    //인텐트에서 받아올 정보
    lateinit var appointmentData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_appointment)
        setValues()
        setupEvents()
    }

    override fun setupEvents() {
        //약속 날짜
        binding.dateTxt.setOnClickListener {
            val dl = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, year: Int, month: Int, date: Int) {
                    //날짜가 선택되고 나면 진행 할 일을 작성
                    Log.d("선택된 시간", "${year}년${month+1}월${date}일")
                    mSelectedDataTime.set(year,month,date)
                    val sdf = SimpleDateFormat("yyyy. MM. dd (E)")
                    //Log.d("변환된 시간", sdf.format(mSelectedDataTime.time))

                    binding.dateTxt.text = sdf.format(mSelectedDataTime.time)
                }
            }


            val dpd = DatePickerDialog(
                mContext, //어느 화면에
                dl, //어떤 리스너
                mSelectedDataTime.get(Calendar.YEAR),//년
                mSelectedDataTime.get(Calendar.MONTH),//월
                mSelectedDataTime.get(Calendar.DATE)//일
            ).show()
        }

        //약속 시간
        binding.timeTxt.setOnClickListener {
            val tl = object : TimePickerDialog.OnTimeSetListener{
                override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
                    Log.d("선택된 시간", "${hour}시, ${minute}분")
                    mSelectedDataTime.set(Calendar.HOUR_OF_DAY, hour)//시간 넣기
                    mSelectedDataTime.set(Calendar.MINUTE, minute)//분 넣기

                    val sdf = SimpleDateFormat("a h:mm")

                    binding.timeTxt.text = sdf.format(mSelectedDataTime.time)
                }
            }
            val tpd = TimePickerDialog(
                mContext,
                tl,
                mSelectedDataTime.get(Calendar.HOUR),
                mSelectedDataTime.get(Calendar.MINUTE),
                false //12시간 짜리로 본다는 뜻
            ).show()
        }

        //시작장소 스피너 선택 이벤트
        binding.startPlaceSpinner
            .onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            //선택했으면 뭐할꺼야?
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                Log.d("스피너 변수 값", "${position}, ${p3}")
                mStartPlace = mStartPlaceList[position]


                //네이버 지도에 출발지 표시할거다
                mNaverMap.let {
                    mStartPlaceMarker.position = LatLng(mStartPlace.latitude, mStartPlace.longitude)
                    mStartPlaceMarker.map = mNaverMap

                    //위위위위위에서 스피너로 선택된 지도객체를 넣어주었기에 사용한다
                    //출발지 선택시 카메라이동하여 보여준다
                    val cameraUpdate = CameraUpdate.scrollTo(LatLng(mStartPlace.latitude, mStartPlace.longitude))
                    mNaverMap!!.moveCamera(cameraUpdate)
                }
            }
            //선택이 안됬으면 어떻게 할거야?
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }


        //약속 수정하기
        binding.saveBtn.setOnClickListener {
            //약속 정함?
            val inputTitle = binding.titleEdt.text.toString()
            if (inputTitle.isBlank()) {
                Toast.makeText(mContext, "약속명을 입력해주세요..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //약속 일시
            if (binding.dateTxt.text == "날짜 선택") {
                Toast.makeText(mContext, "날짜를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //시간 선택
            if (binding.timeTxt.text == "시간 선택") {
                Toast.makeText(mContext, "시간을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //지난날을 선택했나?//  약속날짜까지의 초     지금시간까지의 초 // 약속날짜까지의 지난 초가 많아야 미래임
            if(mSelectedDataTime.timeInMillis < Calendar.getInstance().timeInMillis){
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //약속 장소 이름/명
            val inputPlaceName = binding.placeNameEdt.text.toString()
            if (inputPlaceName.isBlank()) {
                Toast.makeText(mContext, "약속 장소명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            //선택한 친구 목록 가공해서 넣을거
            var friendListStr = ""
            for(friend in appointmentData.invitedFriends){
                friendListStr += friend.id
                friendListStr += ","
            }
            if(friendListStr != ""){
                friendListStr = friendListStr.substring(0, friendListStr.length - 1)
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm")

            //서버에 저장할 정보 넣기
            apiList.putRequestChangeAppointment(
                appointmentData.id.toString(), inputTitle,sdf.format(mSelectedDataTime.time), inputPlaceName,
                mSelectedLatLng!!.latitude, mSelectedLatLng!!.longitude,
                mStartPlace.name, mStartPlace.latitude, mStartPlace.longitude,friendListStr
            ).enqueue(object : Callback<BasicResponse> {
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        Toast.makeText(mContext, "약속이 수정되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })
        }

        //지도 영역에 손을 대면 => 스크롤  뷰 정지!!
        //그래서 지도에 터치이벤트를 준건데 지도에 주면 지도에도 문제가 생기기에 지도에 TextVIew를 겹치고
        //TextView를 터치하면 스크롤뷰가 정지되도록
        binding.scrollHelpTxt.setOnTouchListener { view, motionEvent ->
            //스크롤뷰 터치 이벤트를 막아준다//scrollHelpTxt.setOnTouchListener가 되면 스크롤뷰가 동작 ㄴㄴ
            binding.scrollView.requestDisallowInterceptTouchEvent(true)

            //터치 이벤트만 먹히게 할거냐? => no/ 뒤에 있는 지도 동작도 같이 실행
            return@setOnTouchListener false
        }

    }

    override fun setValues() {
        titleTxt.text = "약속 수정하기"
        backIcon.visibility = View.VISIBLE
        appointmentData = intent.getSerializableExtra("appointmentData") as AppointmentData

        //스피너 이용한 출발장소 리스트 만들어주기
        mPlaceSpinnerAdapter = PlaceSpinnerAdapter(mContext, R.layout.place_list_item, mStartPlaceList)
        binding.startPlaceSpinner.adapter = mPlaceSpinnerAdapter

        //스피너 이용한 출발장소를 서버에서 불러오는
        getStartPlaceDataFromServer()

        //지도객체
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync {

            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            //중간지점을 카메라 보여주기
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            if(mNaverMap == null){
                mNaverMap = it
            }

            var cameralatitude = 0.0
            var cameraLongitude = 0.0
            if(appointmentData.latitude < appointmentData.startLatitude){
                cameralatitude = appointmentData.latitude + (appointmentData.startLatitude-appointmentData.latitude)/2
            }else{
                cameralatitude = appointmentData.startLatitude + (appointmentData.latitude-appointmentData.startLatitude)/2
            }
            if(appointmentData.longitude < appointmentData.startLongitude){
                cameraLongitude = appointmentData.longitude + (appointmentData.startLongitude-appointmentData.longitude)/2
            }else{
                cameraLongitude = appointmentData.startLongitude + (appointmentData.longitude-appointmentData.startLongitude)/2
            }
            val senterCoord = LatLng(cameralatitude, cameraLongitude)
            val cameraPosition = CameraPosition(senterCoord, 10.0)
            mNaverMap!!.cameraPosition = cameraPosition

            Log.d("문제 출발","${appointmentData.startLatitude} ${appointmentData.startLongitude}")
            Log.d("문제 도착","${appointmentData.latitude} ${appointmentData.longitude}")
            Log.d("문제 중간","${cameralatitude} ${cameraLongitude}")
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


            //이게 내가 찍은 좌표
            val coord = LatLng(appointmentData.latitude, appointmentData.longitude)
            val marker = Marker()
            marker.position = coord
            marker.icon = OverlayImage.fromResource(R.drawable.red_marker)
            marker.map = mNaverMap//마커를 네이버 지도에 올리기



            //여긴 도착지점 좌표찍는거다
            mNaverMap!!.setOnMapClickListener { pointF, latLng ->
                mSelectedLatLng = latLng//내가 사용할 변수에 좌표 담기
                marker.position = latLng//마커 좌표 알려주기
                marker.map = mNaverMap//마커를 네이버 지도에 올리기
            }

        }
        var friend = ""
        if(appointmentData.invitedFriends != null) {
            for (UserData in appointmentData.invitedFriends) {
                friend += UserData.nickname.toString()+", "
            }
            friend = friend.substring(0, friend.length-2)

        }
        binding.titleEdt.setText(appointmentData.title)
        binding.friendTxt.text = "인원 : ${ appointmentData.invitedFriends.size.toString()}명 (${friend})"
        binding.placeNameEdt.setText(appointmentData.place)

    }





    //출발장소 데이터 불러오기
    fun getStartPlaceDataFromServer(){
        apiList.getRequestUserPlace().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    mStartPlaceList.clear()
                    mStartPlaceList.addAll(response.body()!!.data.places)
                    mPlaceSpinnerAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }
}