package com.example.hsproject


import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.hsproject.adapters.FriendPagerAdapter
import com.example.hsproject.databinding.ActivityMyFriendBinding


class MyFriendActivity : BaseActivity() {

    lateinit var binding : ActivityMyFriendBinding
    lateinit var mPagerAdapter : FriendPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_friend)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        mPagerAdapter = FriendPagerAdapter(supportFragmentManager)
        binding.friendViewPager.adapter = mPagerAdapter
        binding.friendTabLayout.setupWithViewPager(binding.friendViewPager)
    }
}