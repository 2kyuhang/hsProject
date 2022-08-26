package com.example.hsproject


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.hsproject.adapters.FriendPager2Adapter
import com.example.hsproject.adapters.FriendPagerAdapter
import com.example.hsproject.databinding.ActivityMyFriendBinding
import com.google.android.material.tabs.TabLayoutMediator


class MyFriendActivity : BaseActivity() {

    lateinit var binding : ActivityMyFriendBinding
    //lateinit var mPagerAdapter : FriendPagerAdapter
    lateinit var mPagerAdapter : FriendPager2Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_friend)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        addIcon.setOnClickListener {
            val myIntent = Intent(mContext, SearchUserActivity::class.java)
            startActivity(myIntent)
        }
    }

    //여기서 친구 목록과 요청받은 목록 프레그먼트 받는다
    override fun setValues() {
        backIcon.visibility = View.VISIBLE
        addIcon.visibility = View.VISIBLE

        mPagerAdapter = FriendPager2Adapter(this)
        binding.friendViewPager.adapter = mPagerAdapter
        //binding.friendTabLayout.setupWithViewPager(binding.friendViewPager)

        //이건 다시보기!!
        TabLayoutMediator(binding.friendTabLayout, binding.friendViewPager){ tab, position ->
            when(position){
                0 -> tab.text = "내 친구 목록"
                else -> tab.text = "친구 요청 확인"
            }
        }.attach()
    }
}