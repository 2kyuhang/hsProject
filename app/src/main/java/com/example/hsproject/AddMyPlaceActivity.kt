package com.example.hsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityAddMyPlaceBinding
import com.example.hsproject.datas.BasicResponse
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddMyPlaceActivity : BaseActivity() {

    lateinit var bindng : ActivityAddMyPlaceBinding

    //좌표값
    var mSelectedLatitude = 37.5779235853308
    var mSelectedLongitude = 127.033553463647

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindng = DataBindingUtil.setContentView(this, R.layout.activity_add_my_place)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        bindng.saveBtn.setOnClickListener {
            val inputName = bindng.placeNameEdt.text.toString()
            if(inputName.isBlank()){
                Toast.makeText(mContext, "이름은 공백일 수 없습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //여기 수정하기...뭐지???
            apiList.getRequestAddUserPlace(
                inputName, mSelectedLatitude,mSelectedLongitude, "false"
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                    if(response.isSuccessful){
                        Toast.makeText(mContext, "장소 들록이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })

        }
    }

    override fun setValues() {

        backIcon.visibility = View.VISIBLE
        titleTxt.text = "출방장소 추가하기"

        //지도객체
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

            //마커생성
            val marker = Marker()
            marker.position = coord
            marker.map = naverMap

            naverMap.setOnMapClickListener { pointF, latLng ->
                Log.d("클릭된 위/경도", "위도 : ${latLng.latitude}, 경도 : ${latLng.longitude}")

                val clickedLatLng = latLng
                //클릭시마다 마커 변경됨
                marker.position = latLng
                marker.map = naverMap

/*              클릭시 마커 생겨남*/
/*                val newMarker = Marker()
                newMarker.position = latLng
                newMarker.map = naverMap*/

                mSelectedLatitude = latLng.latitude
                mSelectedLongitude = latLng.longitude

            }

        }

    }

}