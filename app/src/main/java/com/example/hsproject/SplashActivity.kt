package com.example.hsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.utils.ContextUtil
import com.example.hsproject.utils.GlobalData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SplashActivity : BaseActivity() {

    var isTokenOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        val token = ContextUtil.getLoginToken(mContext)
        apiList.getRequestMyInfo(token).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                //SharedPreferences 가 가지고 있는 토큰값이 유효하면!!!
                if(response.isSuccessful){
                    isTokenOk = true //토큰이 있다는것에 true 넣어주고
                    GlobalData.loginUser = response.body()!!.data.user //로그인시 글로벌에 담아 자유롭게 사용한다
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun setValues() {
        val myHandler = Handler(Looper.getMainLooper())

        myHandler.postDelayed(
            {

                //ContextUtil 에서 SharedPreferences 통해 저장해둔 자동로그인 값을 가져온다
                val isAutoLogin = ContextUtil.getAutoLogin(mContext)

                //토큰값과 오토로그인이 둘다 true여야 합격
                if (isTokenOk && isAutoLogin) {
                    Toast.makeText(mContext, "${GlobalData.loginUser!!.nickname}님 환영합니다.", Toast.LENGTH_SHORT).show()
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