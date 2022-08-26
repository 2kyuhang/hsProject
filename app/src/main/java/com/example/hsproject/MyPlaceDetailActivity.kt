package com.example.hsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityMyPlaceDetailBinding
import com.example.hsproject.datas.PlaceData
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker

class MyPlaceDetailActivity : BaseActivity() {

    lateinit var binding : ActivityMyPlaceDetailBinding
    lateinit var mapView : MapView

    lateinit var placeData : PlaceData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_place_detail)

        mapView = binding.mapView
        mapView.onCreate(savedInstanceState) //네이버 지도 // 번들값 넣어주기..

        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        //여긴 정보를 인텐트로 가져온다
        placeData = intent.getSerializableExtra("placeData") as PlaceData

        binding.placeNameTxt.text = placeData.name

        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.mapView) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.mapView, it).commit()
            }

        mapFragment.getMapAsync {
            val naverMap = it


            val coord = LatLng(placeData.latitude, placeData.longitude)

            val cameraUpdata = CameraUpdate.scrollTo(coord)

            naverMap.moveCamera(cameraUpdata)

            val marker = Marker()
            marker.position = coord
            marker.map = naverMap

        }

    }
}