package com.example.hsproject.api

import com.example.hsproject.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.*

interface APIList {
    //회원가입
    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignUp(
        @Field("email")email : String,
        @Field("password")pw : String,
        @Field("nick_name")nick : String
    ):Call<BasicResponse>
    //중복체크 / 이메일 타입 or 닉네임 타입으로 구분
    @GET("/user/check")
    fun getReguestDupCheck(
        @Query("type")type : String,
        @Query("value")value : String
    ):Call<BasicResponse>

    //로그인
    @FormUrlEncoded
    @POST("/user")
    fun postRequestLogin(
        @Field("email")email : String,
        @Field("password") password : String
    ):Call<BasicResponse>

    //토큰으로 회원정보 불러오기
    @GET("/user")
    fun getRequestMyInfo(
        @Header("X-Http-Token") token : String
    ):Call<BasicResponse>

}