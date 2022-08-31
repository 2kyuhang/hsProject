package com.example.hsproject.datas.odsaydatas

data class SubPathData(//모르는거 https://lab.odsay.com/guide/releaseReference#searchPubTransPathT 여기서 확인
    val subPath : Int, //1-지하철, 2-버스, 3-도보 이거에 따라 좌표 따로 받기
    val startX : Double,
    val startY : Double,
    val endX : Double,
    val endY : Double
)
