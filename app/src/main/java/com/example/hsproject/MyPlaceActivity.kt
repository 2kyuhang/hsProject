package com.example.hsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    override fun setupEvents() {

    }

    override fun setValues() {
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
                TODO("Not yet implemented")
            }

        })
    }

}