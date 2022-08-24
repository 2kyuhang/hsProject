package com.example.hsproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsproject.R
import com.example.hsproject.adapters.FriendRecycleViewAdapter
import com.example.hsproject.databinding.FragmentRequestedFriendsBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.UserData
import com.example.hsproject.utils.ContextUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RequestedFriendFragment  : BaseFragment(){

    lateinit var binding : FragmentRequestedFriendsBinding
    lateinit var mFriendAdapter : FriendRecycleViewAdapter
    val mFriendList = ArrayList<UserData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_requested_friends, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
        setValues()
    }

    override fun setEvent() {

    }

    override fun setValues() {
        mFriendAdapter = FriendRecycleViewAdapter(mContext, mFriendList, "request")
        binding.friendRecycleView.adapter = mFriendAdapter
        binding.friendRecycleView.layoutManager = LinearLayoutManager(mContext)
    }

    fun getRequestFriendListFromServer(){
        val token = ContextUtil.getLoginToken(mContext)
        apiList.getRequestFriendList(token, "request").enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    val br = response.body()!! //나에게 칝구추가한 유저들 들어 있다

                    mFriendList.clear()
                    mFriendList.addAll(br.data.friends)//friends로 나에게 친추한 유저 들어있음
                    mFriendAdapter.notifyDataSetChanged()//정보 업뎃했으니 새로고침 해주세요
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }

        })
    }

}