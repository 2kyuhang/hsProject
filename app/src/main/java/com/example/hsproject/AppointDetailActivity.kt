package com.example.hsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityAppointDetailBinding
import com.example.hsproject.datas.AppointmentData
import com.example.hsproject.fragments.chatFragment
import com.naver.maps.map.MapView
import java.text.SimpleDateFormat

class AppointDetailActivity : BaseActivity() {

    lateinit var binding :ActivityAppointDetailBinding
    lateinit var mapView : MapView

    lateinit var appointmentData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appoint_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        //약속 수정 //인텐트로 정보 넣어 수정으로 보내주고 => 수정에서 바뀐거 약속 ID추가해서 되돌리기(여기 화면로 새로고침)
        binding.appointmentChangeBtn.setOnClickListener {

        }

        //대화창 //약속리사티클러어답터에서 인텐트로 받은 정보를 대화창을 위해 한번 더 인텐트로 보낸다
        //시간여유 생기면 필요한것만 넘기기
        messageIcon.setOnClickListener {
            val myIntent = Intent(mContext, chatFragment::class.java)
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

    }
}