package com.example.hsproject.datas

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserData(
    val id : Int,
    val email : String,
    @SerializedName("ready_minute")
    val readyMinute : Int,
    @SerializedName("nick_name")
    val nickname : String,
    @SerializedName("profile_img")
    val profileImg : String,
    val provider : String
) :Serializable
