package com.example.hsproject

import com.example.hsproject.datas.BasicResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PUT

interface APIList {

    @FormUrlEncoded
    @PUT("/user")
    fun putRequestSignUp(
        @Field("email")email : String,
        @Field("password")pw : String,
        @Field("nick_name")nick : String
    ):Call<BasicResponse>

}