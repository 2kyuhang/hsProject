package com.example.hsproject.datas

import android.util.Log
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
//start_latitude
//start_longitude
data class AppointmentData(
    val id : Int,
    val title : String,
    val datetime : Date,
    val place : String,
    @SerializedName("start_latitude")
    val startLatitude : Double,
    @SerializedName("start_longitude")
    val startLongitude : Double,
    val latitude : Double,
    val longitude : Double,
    @SerializedName("invited_friends") //이건 정보를 받아올때 쉽게 가져오기위한 용도?
    val invitedFriends : List<UserData>
) : Serializable

