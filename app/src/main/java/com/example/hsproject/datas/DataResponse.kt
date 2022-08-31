package com.example.hsproject.datas

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class DataResponse(
    val token : String,
    val user : UserData,
    val friends : List<UserData>,
    val users : List<UserData>,
    val places : List<PlaceData>,
    val appointments : List<AppointmentData>,
    val appointment : AppointmentData,
    @SerializedName("invited_appointments")
    val invitedAppointments: List<InvitedAppointmentsData>
) :Serializable
