package com.example.hsproject

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

//가장 먼저 실행
class GlobalApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, "dfcaa18a7f30ae84e9d33b9d096fcd26")
    }
}