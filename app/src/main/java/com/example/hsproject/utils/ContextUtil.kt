package com.example.hsproject.utils

import android.content.Context

class ContextUtil {

    companion object {

        private val prefName = "KeepThePref"
        private val LOGIN_TOKEN = "LOGIN_KOKEN"
        private val AUTO_LOGIN = "AUTO_LOGIN"

        fun setLoginToken(context : Context, token : String){
            //메모장 열기
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
            //메모장 작성하기 //스트링 넣을거 이름/ 토큰 넣기
            pref.edit().putString(LOGIN_TOKEN, token).apply()
        }

        fun getLoginToken(context : Context):String{
            //메모장 열기
            val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
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

    }

}