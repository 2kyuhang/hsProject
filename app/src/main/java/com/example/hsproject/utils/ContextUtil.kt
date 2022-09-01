package com.example.hsproject.utils

import android.content.Context
import android.util.Log

class ContextUtil {

    companion object {

        private val prefName = "KeepThePref"
        private val LOGIN_TOKEN = "LOGIN_TOKEN"
        private val AUTO_LOGIN = "AUTO_LOGIN"
        private val DEVICE_TOKEN = "DEVICE_TOKEN"

        fun setLoginToken(context : Context, token : String){
            //메모장 열기
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            //메모장 작성하기 //스트링 넣을거 이름/ 토큰 넣기
            //ㄴLog.d("문제 setLoginToken 들어감 ","${token}")
            pref.edit().putString(LOGIN_TOKEN, token).apply()
        }

        fun getLoginToken(context : Context):String{
            //메모장 열기
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            //Log.d("문제 getLoginToken 들어감 ","${pref.getString(LOGIN_TOKEN, "")}")
            return pref.getString(LOGIN_TOKEN, "")!! //없으면 빈칸 던져줌
        }

        fun setAutoLogin(context : Context, autoLogin : Boolean){
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putBoolean(AUTO_LOGIN, autoLogin).apply()
        }

        fun getAutoLogin(context: Context) : Boolean{
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getBoolean(AUTO_LOGIN, false)
        }

        //스플래쉬액티비티에서 처음에 뽑아온다
        fun setDeviceToken(context : Context, token : String) {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().putString(DEVICE_TOKEN, token).apply()
        }

        fun getDeviceToken(context: Context) : String {
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            return pref.getString(DEVICE_TOKEN, "")!!
        }

    }

}