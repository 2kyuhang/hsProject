package com.example.hsproject.api

import android.content.Context
import com.example.hsproject.utils.ContextUtil
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServerAPI {

    companion object{
        private val baseUrl = "https://keepthetime.xyz"
        private var retrofit : Retrofit? = null
        fun getRetrofit(context : Context) : Retrofit {
            //API요청이 발생하면 => 가로채서 => 헤더를 추가
            //인터셉터@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
            val interceptor = Interceptor{
                with(it){
                    val newRequest = request().newBuilder() //리퀘스트 새로 만듬
                        //여기서 mContext를 사용하기 위해 매개변수로 context를 받는다
                        .addHeader("X-Http-Token", ContextUtil.getLoginToken(context))
                        .build()
                    proceed(newRequest)
                }
            }//매번 공통된 토큰을 넣는것을 생략하기 위해 가로채서 넣어주는데 이때 토큰을 가져오기 위해
            //getRetrofit에서 매겨변수로 Context를 가져와서 getLoginToken(context)에 넣어준다
            //인터셉터@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            //gson에게 날짜 양식을 어떻게 파싱할 건지 => 추가 기능을 가진 gson으로 생성
            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create()


            //retrofit는 OKHttp의 확장판 => OkHttpClient 형태의 클라이언트를 활용
            //클라이언트에게 우리가 만든 인터셉터를 달아준다
            val myClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
            //인터셉터 널어주기

            if(retrofit == null){
                retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(myClient)//인터셉터 넣어주기                        //create(gson)) 에서 gson을 넣음으로
                    .addConverterFactory(GsonConverterFactory.create(gson)) // <= gson에서 데이터 형식을 자동으로 바꿔주는 기능을 넣었다
                    .build()
            }
            return retrofit!!
        }
    }
}