package com.example.hsproject

import android.content.Context
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.hsproject.api.APIList
import com.example.hsproject.api.ServerAPI
import retrofit2.Retrofit

abstract class BaseActivity : AppCompatActivity(){

    lateinit var mContext : Context

    //액션바를 이 변수를 통해 자유롭게 사용 가능
    lateinit var backIcon : ImageView
    lateinit var titleTxt : TextView
    lateinit var addIcon : ImageView
    lateinit var messageIcon : ImageView
    lateinit var frameLayout : FrameLayout


    lateinit var retrofit : Retrofit
    lateinit var apiList : APIList

    val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {//가장 먼저 실행되는 onCreate이다
        super.onCreate(savedInstanceState)

        mContext = this

        if(supportActionBar != null){
            setCustomActivityBar()
        }


        retrofit = ServerAPI.getRetrofit(mContext) //싱글톤 만든거 가져오기
        apiList = retrofit.create(APIList::class.java)//레트로핏으로 생성할거에요!!
    }

    abstract fun setupEvents()
    abstract fun setValues()

    //커스텀 액션바 설정
    fun setCustomActivityBar(){
        //기존 액션바를 담아줄 변수
        val defaultActionBar = supportActionBar!! // <=안드로이드에서제공하는 기본 액션바


        //기존 액션바를 커스텀 모드로 변경 > 우리가 만든 xml로 변경
        defaultActionBar.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM//안드로이드 X에서 주는걸로 선택하기
        defaultActionBar.setCustomView(R.layout.custom_action_bar)

        //부모 액션바
        val myToolbar = defaultActionBar.customView.parent as Toolbar //안드로이드 X에서 제공하는 툴바(가 부모다)
        myToolbar.setContentInsetsAbsolute(0, 0)//부모 액션바 여백 0, 0으로!

        titleTxt = defaultActionBar.customView.findViewById<TextView>(R.id.titleTxt)
        addIcon = defaultActionBar.customView.findViewById<ImageView>(R.id.addIcon)
        backIcon = defaultActionBar.customView.findViewById<ImageView>(R.id.backIcon)
        messageIcon = defaultActionBar.customView.findViewById<ImageView>(R.id.messageIcon)
        frameLayout = defaultActionBar.customView.findViewById<FrameLayout>(R.id.frameLayout)

        backIcon.setOnClickListener { finish() }
    }

}