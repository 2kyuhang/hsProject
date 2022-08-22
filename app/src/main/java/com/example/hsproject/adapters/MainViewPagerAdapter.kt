package com.example.hsproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hsproject.fragments.InvitedAppointmentFragment
import com.example.hsproject.fragments.MyAppointmentFragment
import com.example.hsproject.fragments.SettingFragment

//기존 뷰페이저는 FragmentPagerAdapter 를 상속 받았으나
//뷰페이저2 는
//FragmentStateAdapter() 를 상속 받아 사용한다
class MainViewPagerAdapter(fa : FragmentActivity) :  FragmentStateAdapter(fa){

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyAppointmentFragment()
            1 -> InvitedAppointmentFragment()
            else -> SettingFragment()
        }
    }

}