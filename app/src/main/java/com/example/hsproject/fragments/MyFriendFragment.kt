package com.example.hsproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsproject.R
import com.example.hsproject.SearchUserActivity
import com.example.hsproject.adapters.FriendRecycleViewAdapter
import com.example.hsproject.databinding.FragmentMyFriendsBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.UserData
import com.example.hsproject.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyFriendFragment  : BaseFragment(){

    lateinit var binding : FragmentMyFriendsBinding
    lateinit var mFriendAdapter : FriendRecycleViewAdapter
    val mFriendsList = ArrayList<UserData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_friends, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
        setValues()
    }

    override fun setEvent() {
        binding.addFriendBtn.setOnClickListener {
            val myIntent = Intent(mContext, SearchUserActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mFriendAdapter = FriendRecycleViewAdapter(mContext, mFriendsList, "my")
        binding.myFriendRecyclerView.adapter = mFriendAdapter
        binding.myFriendRecyclerView.layoutManager = LinearLayoutManager(mContext)
        getFriendDataFromServer()//서버에서 정보 가져와서 mFriendList에 넣기
    }

    //서버에서 정보 가져와서 mFriendList에 넣기
    fun getFriendDataFromServer(){
        val token = ContextUtil.getLoginToken(mContext)
        apiList.getRequestFriendList(token, "my").enqueue(object :Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    //데이터 중복 방지를 위한 정리? 클리어
                    mFriendsList.clear()

                    val br = response.body()!!

                    mFriendsList.addAll(br.data.friends)
                    mFriendAdapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }

}