package com.example.hsproject

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityAddAppointmentBinding
import com.example.hsproject.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class AddAppointmentActivity : BaseActivity() {

    lateinit var binding: ActivityAddAppointmentBinding

    //네이버 지도 좌표
    var mSelectedLatLng: LatLng? = null

    //선택된 약속 일시를 저장할 변수
    val mSelectedDataTime = Calendar.getInstance()//기본값 : 현재 시간


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appointment)
        setupEvents()
        setValues()
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

        binding.saveBtn.setOnClickListener {
            //약속 정함?
            val inputTitle = binding.titleEdt.toString()
            if (inputTitle.isBlank()) {
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
            //지난날을 선택했나?
            /*if(){
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }*/
            //약속 장소 이름/명
            val inputPlaceName = binding.placeNameEdt.text.toString()
            if (inputPlaceName.isBlank()) {
                Toast.makeText(mContext, "약속 장소명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //약송 장소 좌표
            if (mSelectedLatLng == null) {
                Toast.makeText(mContext, "약속 장소를 지도에서 선택해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //실제 도착 지점을 선택했는가? 네이버 지도
/*            apiList.postRequestAddAppointment(
                inputTitle, inputPlaceName,
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })*/

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
        titleTxt.text = "새 약속 만들기"
        backIcon.visibility = View.VISIBLE

        //지도객체
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.map, it).commit()
            }

        mapFragment.getMapAsync {

            val naverMap = it
            val marker = Marker()

            naverMap.setOnMapClickListener { pointF, latLng ->
                mSelectedLatLng = latLng//내가 사용할 변수에 좌표 담기
                marker.position = latLng//마커 좌표 알려주기
                marker.map = naverMap//마커를 네이버 지도에 올리기
            }

        }

    }
}