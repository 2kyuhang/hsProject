package com.example.hsproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityLoginBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.utils.ContextUtil
import com.example.hsproject.utils.GlobalData
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.LoginBtn.setOnClickListener {

            val inputEmail = binding.emailEdt.text.toString()
            val inputPw = binding.passwordEdt.text.toString()

            apiList.postRequestLogin(
                inputEmail, inputPw
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!

                        //로그인시 글로벌데이터에 앱실행하는 동안만 저장되는 변수에 user을 담아준다
                        GlobalData.loginUser = br.data.user
                        //로그인 화면에서 자동로그인 체크시
                        ContextUtil.setAutoLogin(mContext, binding.autoLoginCb.isChecked)
                        //로그인시 토큰 정보를 SharedPreferences 에 담아두고 사용한다
                        ContextUtil.setLoginToken(mContext, br.data.token)

                        Log.d("문제", "${binding.autoLoginCb.isChecked} ${br.data.token}")

                        Toast.makeText(mContext, "${br.data.user.nickname}님 환영합니다.", Toast.LENGTH_SHORT).show()

                        val myIntent = Intent(mContext, MainActivity::class.java)
                        startActivity(myIntent)
                        finish()

                    }else {
                        val erroBodyStr = response.errorBody()!!.string()
                        val jsonObj = JSONObject(erroBodyStr)
                        val code = jsonObj.getInt("code")
                        val message = jsonObj.getString("message")
                        if(code == 400){
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                    Log.d("로그인 기능 에러", t.toString())
                }
            })
        }

        binding.signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {

    }
}