package com.example.hsproject.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.hsproject.R
import com.example.hsproject.api.APIList
import com.example.hsproject.api.ServerAPI
import retrofit2.Retrofit
import retrofit2.create

abstract class BaseFragment : Fragment(){

    lateinit var mContext : Context
    lateinit var retrofit: Retrofit
    lateinit var apiList : APIList

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mContext = requireContext()
        retrofit = ServerAPI.getRetrofit(mContext)
        apiList = retrofit.create(APIList::class.java)

        
    }

    abstract  fun setEvent()
    abstract fun setValues()


}