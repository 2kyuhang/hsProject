package com.example.hsproject.datas

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PlaceData(
    val id : Int,
    val name : String,
    var latitude : Double,
    var longitude :  Double,
    @SerializedName("is_primary")
    val isPrimary : Boolean
) :Serializable
