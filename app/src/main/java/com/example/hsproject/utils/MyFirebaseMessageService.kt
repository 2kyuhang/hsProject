package com.example.hsproject.utils

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessageService:FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        //알림 오면 할 일 작성

        //data-message양식으로 왔을 경우
        if(message.data.isNotEmpty()){ //내용물이 있니?
            Log.d("FCM","data : ${message.data}")
        }

        //notification양식으로 왔을경우
        message.notification?.let{
            Log.d("FCM","notification : ${it.body}")
            /*메인 스레드에서 작업하는 것*/
            Handler(Looper.getMainLooper()).post{
                Toast.makeText(this, "notification 알림 수신", Toast.LENGTH_SHORT).show()
            }
        }

    }

}