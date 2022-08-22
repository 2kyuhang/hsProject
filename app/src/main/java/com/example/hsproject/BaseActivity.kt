package com.example.hsproject

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hsproject.api.APIList
import com.example.hsproject.api.ServerAPI
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity(){

    lateinit var mContext : Context

    lateinit var retrofit : Retrofit
    lateinit var apiList : APIList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        retrofit = ServerAPI.getRetrofit() //싱글톤 만든거 가져오기
        apiList = retrofit.create(APIList::class.java)//레트로핏으로 생성할거에요!!
    }

    abstract fun setupEvents()
    abstract fun setValues()

}