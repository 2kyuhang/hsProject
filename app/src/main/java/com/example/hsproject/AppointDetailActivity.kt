package com.example.hsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityAppointDetailBinding
import com.example.hsproject.datas.AppointmentData
import com.naver.maps.map.MapView
import java.text.SimpleDateFormat

class AppointDetailActivity : BaseActivity() {

    lateinit var binding :ActivityAppointDetailBinding
    lateinit var mapView : MapView

    lateinit var appointData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_appoint_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        val formatter = SimpleDateFormat("M/dd a h:ss")

/*        appointData = intent.getSerializableExtra("appointData") as AppointmentData
        binding.titleTxt.text = appointData.title
        binding.dateTxt.text = formatter.format(appointData.datetime)*/
    }
}