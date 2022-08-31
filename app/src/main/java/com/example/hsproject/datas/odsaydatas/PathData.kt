package com.example.hsproject.datas.odsaydatas

data class PathData(
    val pathType : Int,//1 지하철, 2 버스, 3 버스+지하철
    val info : InfoData, //이 안에 요금/시간/거리 등등이 들어있음
    val subPath : List<SubPathData>,
)
