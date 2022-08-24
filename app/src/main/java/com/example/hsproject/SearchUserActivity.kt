package com.example.hsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsproject.adapters.SerchUserRecycleViewAdapter
import com.example.hsproject.databinding.ActivitySerchUserBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.UserData
import com.example.hsproject.utils.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchUserActivity : BaseActivity() {
    lateinit var binding: ActivitySerchUserBinding
    lateinit var mUserAdapter : SerchUserRecycleViewAdapter
    val mUserList = ArrayList<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_serch_user)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.serchBtn.setOnClickListener {

            val inputNick = binding.serchEdt.text.toString()
            searchUserFromServer(inputNick)
        }
    }

    override fun setValues() {
        mUserAdapter = SerchUserRecycleViewAdapter(mContext, mUserList)
        binding.serchUserRecyclerView.adapter = mUserAdapter
        binding.serchUserRecyclerView.layoutManager = LinearLayoutManager(mContext)



    }

    fun searchUserFromServer(inputNick : String){
        val token = ContextUtil.getLoginToken(mContext)
        apiList.getRequestSerchUser(inputNick).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!!
                    mUserList.clear()

                    mUserList.addAll(br.data.users)
                    //Log.d("문제 값", "${br.data.users}")
                    if(br.data.users.isEmpty()){
                        Toast.makeText(mContext, "존재하지 않는 사용자입니다.", Toast.LENGTH_SHORT).show()
                    }
                    mUserAdapter.notifyDataSetChanged()
                }else {
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)
                    val message = jsonObj.getString("message")

                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }

}