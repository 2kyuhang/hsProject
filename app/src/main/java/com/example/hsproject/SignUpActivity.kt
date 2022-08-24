package com.example.hsproject

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivitySignUpBinding
import com.example.hsproject.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {

    lateinit var binding : ActivitySignUpBinding

    var isEmailOk = true
    var isNickOk = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.signUpBtn.setOnClickListener {
            //아이디 공백
            val inputEmail = binding.emailEdt.text.toString()
            if(inputEmail.isBlank()){
                Toast.makeText(mContext, "이메일 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //비밀번호 공백
            val inputPw = binding.passwordEdt.text.toString()
            if(inputPw.isBlank()){
                Toast.makeText(mContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //비밀번호 일치?
            val checkPw = binding.checkPwEdt.text.toString()
            if(inputPw != checkPw){
                Toast.makeText(mContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //닉네임 공백
            val inputNick = binding.nickEdt.text.toString()
            if(inputNick.isBlank()){
                Toast.makeText(mContext, "닉네임을 입력해주세요..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //이메일과 닉네임 중복 체크
            if(!isEmailOk || !isNickOk){
                Toast.makeText(mContext, "중복 검사 진행해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //서버 연결//부모에 있는 apiList로
            apiList.putRequestSignUp(
                inputEmail, inputPw, inputNick
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {
                    if(response.isSuccessful){
                        val br = response.body()!!
                        Toast.makeText(mContext, "회원가입을 축하합니다..", Toast.LENGTH_SHORT).show()
                        finish()
                    }else {

                    }
                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }
            })

        }

        binding.emailDupBtn.setOnClickListener {
            val inputEmail = binding.emailEdt.text.toString()
            if(inputEmail.isBlank()){
                Toast.makeText(mContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dupCheck("EMAIL", inputEmail)
        }
        binding.nickDupBtn.setOnClickListener {
            val inputNick = binding.nickEdt.text.toString()
            if(inputNick.isBlank()){
                Toast.makeText(mContext, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            dupCheck("NICK_NAME", inputNick)
        }
    }

    override fun setValues() {

    }

    fun dupCheck(type : String, value : String){
        apiList.getReguestDupCheck(type, value).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                if(response.isSuccessful){
                    when(type){
                        "EMAIL" -> {
                            Log.d("문제", "200인데 알림은 안뜸")
                            isEmailOk = true
                            Toast.makeText(mContext, "사용 가능한 이메일입니다..", Toast.LENGTH_SHORT).show()
                        }
                        "NICK_NAME" -> {
                            isNickOk = true
                            Toast.makeText(mContext, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else {
                    when(type){
                        "EMAIL" -> {
                            Log.d("문제", "400인데 알림은 안뜸")
                            isEmailOk = false
                            Toast.makeText(mContext, "사용 중인 이메일입니다.", Toast.LENGTH_SHORT).show()
                        }
                        "NICK_NAME" -> {
                            isNickOk = false
                            Toast.makeText(mContext, "사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

            }
        })
    }

}