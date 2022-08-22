package com.example.hsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class SplashActivity : BaseActivity() {

    var isTokenOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed(
            {
                if (isTokenOk) {
                    var myIntent = Intent(mContext, MainActivity::class.java)
                    startActivity(myIntent)
                } else {
                    var myIntent = Intent(mContext, LoginActivity::class.java)
                    startActivity(myIntent)
                }
                finish()
            }, 2000)
    }
}