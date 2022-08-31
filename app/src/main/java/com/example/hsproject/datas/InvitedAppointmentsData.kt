package com.example.hsproject.datas

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class InvitedAppointmentsData(
    val id : Int,
    @SerializedName("user_id")
    val userId : Int,
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
    val invitedFriends : List<UserData>,
    val user : UserData
) : Serializable
