package com.example.hsproject.datas

import java.io.Serializable

data class DataResponse(
    val token : String,
    val user : UserData,
    val friends : List<UserData>,
    val users : List<UserData>,
    val places : List<PlaceData>,
    val appointments : List<AppointmentData>
) :Serializable
