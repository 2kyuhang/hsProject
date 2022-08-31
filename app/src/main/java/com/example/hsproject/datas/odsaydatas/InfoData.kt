package com.example.hsproject.datas.odsaydatas

data class InfoData(
    val totalTime : Int,//총 소요시간
    val payment : Int, //총 요금
    val totalDistance : Double, //총 거리
    val firstStartStation : String,
    val lastEndStation : String
){
    fun getTotalDistance():String{

        var result =""
        if(totalDistance < 1000){
            result = "${totalDistance}+m"
        }else {
            result = "${totalDistance/1000}km"
        }
        return result
    }

}