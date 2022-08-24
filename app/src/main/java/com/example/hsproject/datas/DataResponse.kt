package com.example.hsproject.datas

data class DataResponse(
    val token : String,
    val user : UserData,
    val friends : List<UserData>,
    val users : List<UserData>,
    val places : List<PlaceData>
)
