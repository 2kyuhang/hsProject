package com.example.hsproject

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivitySignUpBinding

class SignUpActivity : BaseActivity() {

    lateinit var binding : ActivitySignUpBinding

    var isEmailOk = false
    var isNickOk = false

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
                Toast.makeText(mContext, "이메일 입력해주세요.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            //비밀번호 공백
            val inputPw = binding.passwordEdt.text.toString()
            if(inputPw.isBlank()){
                Toast.makeText(mContext, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }
            //비밀번호 일치?
            val checkPw = binding.checkPwEdt.text.toString()
            if(inputPw != checkPw){
                Toast.makeText(mContext, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            //닉네임 공백
            val inputNick = binding.nickEdt.text.toString()
            if(inputNick.isBlank()){
                Toast.makeText(mContext, "닉네임을 입력해주세요..", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            //이메일과 닉네임 중복 체크
            if(!isEmailOk || !isNickOk){
                Toast.makeText(mContext, "중복 검사 진행해주세요.", Toast.LENGTH_SHORT)
                return@setOnClickListener
            }

            //서버 연결
        }
    }

    override fun setValues() {
        TODO("Not yet implemented")
    }
}