package com.example.hsproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityAppointDetailBinding
import com.example.hsproject.datas.AppointmentData
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.fragments.MyAppointmentFragment
import com.example.hsproject.fragments.chatFragment
import com.example.hsproject.utils.ContextUtil
import com.naver.maps.map.MapView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class AppointDetailActivity : BaseActivity() {

    lateinit var binding :ActivityAppointDetailBinding
    lateinit var mapView : MapView

    lateinit var appointmentData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appoint_detail)
        setValues()
        setupEvents()
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
                    apiList.deleteAppointment(appointmentData.id.toString()).enqueue(object : Callback<BasicResponse>{
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

        var friend = ""
        if(appointmentData.invitedFriends != null) {
            for (UserData in appointmentData.invitedFriends) {
                friend += UserData.nickname.toString()+", "
            }
            friend = friend.substring(0, friend.length-2)

        }

        binding.friendTxt.text = "인원 : ${ appointmentData.invitedFriends.size.toString()}명 (${friend})"

    }



}