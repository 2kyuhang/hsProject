package com.example.hsproject

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityLoginBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.utils.ContextUtil
import com.example.hsproject.utils.GlobalData
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
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
        //로그인
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
        //회원가입
        binding.signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext, SignUpActivity::class.java)
            startActivity(myIntent)
        }
        //카카오 로그인
        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }
        //비밀번호 찾기
        binding.searchPwTxt.setOnClickListener {
            // 커스텀 뷰 만든거 변수화
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)
            //뷰 수정
            val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
            inputEdt.visibility = View.GONE//닉네임 변경창 숨기기
            val searchLayout = customView.findViewById<LinearLayout>(R.id.searchPwLayout)
            searchLayout.visibility = View.VISIBLE
            val emailEdt = customView.findViewById<EditText>(R.id.emailEdt)
            val nickEdt = customView.findViewById<EditText>(R.id.nickEdt)

            //알럿 창!! 경고창!!
            val alert = AlertDialog.Builder(mContext)
                .setTitle("비밀번호 찾기")
                .setMessage("이메일과 닉네임을 통해 비밀번호 찾기")
                .setView(customView) // 뷰 넣기
                //확인버튼 선택시
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                    apiList.searchPw(emailEdt.text.toString(), nickEdt.text.toString()).enqueue(object :Callback<BasicResponse>{
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){
                                Toast.makeText(mContext, "임시 비밀번호가 ${emailEdt.text.toString()} 로 전송되었습니다.",Toast.LENGTH_SHORT)
                                    .show()
                            }else{
                                Toast.makeText(mContext, "일치하는 사용자가 없습니다.",Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        }

                    })
                })
                //취소버튼 선택시
                .setNegativeButton("취소", null)
                .show()


        }

    }

    override fun setValues() {
        //frameLayout.background = BackgroundColorSpan

        frameLayout.setBackgroundColor(Color.WHITE)
    }

    fun kakaoLogin(){
        // 로그인 조합 예제

// 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                getKakaoUserInfo()
            }
        }

// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(mContext)) {
            UserApiClient.instance.loginWithKakaoTalk(mContext) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    getKakaoUserInfo()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(mContext, callback = callback)
        }
    }

    fun getKakaoUserInfo(){
        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}")
                setSocialLogin("kakao", "${user.id}","${user.kakaoAccount?.profile?.nickname}")
            }
        }
    }
    fun setSocialLogin(provider : String, uid : String, nickname : String){
        apiList.postRequestSocialLogin(provider, uid, nickname).enqueue(object : Callback<BasicResponse>{
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
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

            }

        })
    }
}