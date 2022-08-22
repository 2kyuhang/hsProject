package com.example.hsproject.api

import com.example.hsproject.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.*

interface APIList {

    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignUp(
        @Field("email")email : String,
        @Field("password")pw : String,
        @Field("nick_name")nick : String
    ):Call<BasicResponse>

    @GET("/user/check")
    fun getReguestDupCheck(
        @Query("type")type : String,
        @Query("value")value : String
    ):Call<BasicResponse>

    @POST("/user")
    fun postRequestLogin(
        @Field("email")email : String,
        @Field("password") password : String
    ):Call<BasicResponse>

}