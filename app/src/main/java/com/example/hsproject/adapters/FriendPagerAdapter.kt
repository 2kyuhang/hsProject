package com.example.hsproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.hsproject.fragments.MyFriendFragment
import com.example.hsproject.fragments.RequestedFriendFragment

//뷰페이저 1
class FriendPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm){

    override fun getCount(): Int {
        return 2
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> MyFriendFragment()
            else -> RequestedFriendFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "내 친구 목록"
            else -> "친구 요청 확인"
        }
    }

}