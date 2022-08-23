package com.example.hsproject.api

import com.example.hsproject.datas.BasicResponse
import okhttp3.MultipartBody
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

    //프로필 넣기
    @Multipart //멀티파트(이미지 넣기) 쓸거니깐!
    @PUT("/user/image")
    fun putRequestUserImg(
        @Header("X-http-Token")token: String,
        @Part img: MultipartBody.Part //여기는 이미지 넣을때 이미 어떻게 넣을건지 적었기 때문!
    ):Call<BasicResponse>

    //닉네임 변경
    @FormUrlEncoded
    @PATCH("/user")
    fun patchRequestEditUserData(
        @Header("X-http-Token")token: String,
        @Field("field")field: String,
        @Field("value") value: String
    ):Call<BasicResponse>

    //비밀번호 변경
    @FormUrlEncoded
    @PATCH("/user/password")
    fun patchRequestChangePassword(
        @Header("X-http-Token")token: String,
        @Field("current_password")currentPw : String,
        @Field("new_password") newPw : String
    ):Call<BasicResponse>

    @GET("/user/friend")
    fun getRequestFriendList(
        @Header("X-http-Token")token: String,
        @Query("type")type : String
    ):Call<BasicResponse>

}