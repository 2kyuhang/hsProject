package com.example.hsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsproject.adapters.PlaceRecyclerAdapter
import com.example.hsproject.databinding.ActivityMyPlaceBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPlaceActivity : BaseActivity() {

    lateinit var binding : ActivityMyPlaceBinding
    lateinit var mPlaceAdapter : PlaceRecyclerAdapter
    val mPlaceList = ArrayList<PlaceData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_place)
        setupEvents()
        setValues()
    }

    override fun onResume(){ //화면이 다시 보여지게 되면
        super.onResume()
        getPlaceListFromServer()//정보 업데이트
    }

    override fun setupEvents() {
/*        binding.addPlaceBtn.setOnClickListener {
            var myIntent = Intent(mContext, AddMyPlaceActivity::class.java)
            startActivity(myIntent)
        }*/
        addIcon.setOnClickListener {
            var myIntent = Intent(mContext, AddMyPlaceActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        //장소 추가버튼 지우고 액션바에 만듬
        addIcon.visibility = View.VISIBLE
        backIcon.visibility = View.VISIBLE
        titleTxt.text = "나의 출발지"

        mPlaceAdapter = PlaceRecyclerAdapter(mContext, mPlaceList)
        binding.placeRecyclerView.adapter = mPlaceAdapter
        binding.placeRecyclerView.layoutManager = LinearLayoutManager(mContext)
        getPlaceListFromServer()
    }

    fun getPlaceListFromServer(){
        apiList.getRequestUserPlace().enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    var br = response.body()!!

                    mPlaceList.clear()
                    mPlaceList.addAll(br.data.places)
                    mPlaceAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

}