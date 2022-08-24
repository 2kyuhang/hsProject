package com.example.hsproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hsproject.fragments.MyFriendFragment
import com.example.hsproject.fragments.RequestedFriendFragment

class FriendPager2Adapter(fa : FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyFriendFragment()
            else -> RequestedFriendFragment()
        }
    }

}