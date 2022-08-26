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
    ):Call<BasicResponse>

    //프로필 넣기
    @Multipart //멀티파트(이미지 넣기) 쓸거니깐!
    @PUT("/user/image")
    fun putRequestUserImg(//X-Http-Token
        @Part img: MultipartBody.Part //여기는 이미지 넣을때 이미 어떻게 넣을건지 적었기 때문!
    ):Call<BasicResponse>

    //닉네임 변경
    @FormUrlEncoded
    @PATCH("/user")
    fun patchRequestEditUserData(
        @Field("field")field: String,
        @Field("value") value: String
    ):Call<BasicResponse>

    //비밀번호 변경
    @FormUrlEncoded
    @PATCH("/user/password")
    fun patchRequestChangePassword(
        @Field("current_password")currentPw : String,
        @Field("new_password") newPw : String
    ):Call<BasicResponse>

    //친구목록가져오기
    @GET("/user/friend")
    fun getRequestFriendList(
        @Query("type")type : String
    ):Call<BasicResponse>

    //친구목록가져오기
    @GET("/search/user")
    fun getRequestSerchUser(
        @Query("nickname")nickname : String
    ):Call<BasicResponse>

    //친구요청
    @FormUrlEncoded //폼데이터 활용하고 있다
    @POST("/user/friend")
    fun postRequestAddFriend(
        @Field("user_id")id : Int
    ) : Call<BasicResponse>

    //친구 요청 수락/거절
    @FormUrlEncoded
    @PUT("/user/friend")
    fun putRequestAddFriend(
        @Field("user_id")id : Int,
        @Field("type")type:String
    ) : Call<BasicResponse>

    @GET("/user/place")
    fun getRequestUserPlace():Call<BasicResponse>

    @FormUrlEncoded
    @POST("/user/place")
    fun getRequestAddUserPlace(
        @Field("name") name : String,
        @Field("latitude") latitude : Double,
        @Field("longitude") longitude : Double,
        @Field("is_primary")isPrimary: String
    ):Call<BasicResponse>

}