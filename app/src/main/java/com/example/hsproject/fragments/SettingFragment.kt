package com.example.hsproject.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.Dimension
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hsproject.*
import com.example.hsproject.databinding.FragmentSettingBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.utils.ContextUtil
import com.example.hsproject.utils.GlobalData
import com.example.hsproject.utils.SizeUtil.Companion.writeBitmap
import com.example.hsproject.utils.URIPathHelper
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class SettingFragment : BaseFragment(){

    lateinit var binding : FragmentSettingBinding
    private val REQU_FOR_GALLARY = 1000

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
        setValues()
    }

    override fun setEvent() {
        //프로필 이미지 변경
        binding.profileImg.setOnClickListener {
            val pl = object : PermissionListener{
                override fun onPermissionGranted() {
                    //아래 코드는 복붙하기

                    val myIntent = Intent()
                    myIntent.action = Intent.ACTION_PICK //액션 픽 기능을 통해서
                    //             안드로이드가 제공하는것중에 미디어중에 이미지를 들고 올거에요
                    myIntent.type = android.provider.MediaStore.Images.Media.CONTENT_TYPE
                    startActivityForResult(myIntent, REQU_FOR_GALLARY)
                }
                override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                    //실패시
                    Toast.makeText(mContext, "갤러리 조회 권한이 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            TedPermission.create()
                .setPermissionListener(pl)
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()

        }
        //닉네임 변경
        binding.editNickLayout.setOnClickListener {

            // 뷰 만든거 변수화
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)

            //토큰 가져오기
            val token = ContextUtil.getLoginToken(mContext)

            //알럿 창!! 경고창!!
            val alert = AlertDialog.Builder(mContext)
                .setTitle("닉네임 변경")
                .setView(customView) // 뷰 넣기
                    //확인버튼 선택시
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->
                    //customView 에 있는 inputEdt를 찾아와서 그 내용값을 inputNick에 담아서 서버로 전달

                    //알럿창의 입력값 가져오기
                    val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)

                    val inputNick = inputEdt.text.toString()

                    //inputNick 변수 생성 > EditText 값을 대입(inputNick이 공백일 경우 toast전달 및 클릭 리스너 탈출)
                        apiList.patchRequestEditUserData("nickname", inputNick).enqueue(object :Callback<BasicResponse>{
                            override fun onResponse(
                                call: Call<BasicResponse>,
                                response: Response<BasicResponse>
                            ) {
                                if(response.isSuccessful){ //200시
                                    Toast.makeText(mContext, "닉네임이 변경되었습니다.",Toast.LENGTH_SHORT)
                                        .show()
                                    //글로벌에도 닉 변경한거 넣어주기
                                    GlobalData.loginUser = response.body()!!.data.user
                                    //TextView에 넣어주기
                                    binding.nickTxt.text = GlobalData.loginUser!!.nickname
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

        //시간변경
        binding.readyMinuteLayout.setOnClickListener {
            // 뷰 만든거 변수화
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)
            //알럿창의 입력값 가져오기
            val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
            inputEdt.inputType = InputType.TYPE_CLASS_NUMBER

            //토큰 가져오기
            val token = ContextUtil.getLoginToken(mContext)

            //알럿 창!! 경고창!!
            val alert = AlertDialog.Builder(mContext)
                .setTitle("준비시간 변경")
                .setView(customView) // 뷰 넣기
                //확인버튼 선택시
                .setPositiveButton("변경", DialogInterface.OnClickListener { dialogInterface, i ->
                    //customView 에 있는 inputEdt를 찾아와서 그 내용값을 inputNick에 담아서 서버로 전달


                    val inputReadyMinute = inputEdt.text.toString()

                    if(inputReadyMinute.toInt() <= 30){
                        Toast.makeText(mContext, "30분 이후 시간(숫자)만 입력 가능합니다.",Toast.LENGTH_SHORT)
                            .show()
                        return@OnClickListener
                    }

                    //inputNick 변수 생성 > EditText 값을 대입(inputNick이 공백일 경우 toast전달 및 클릭 리스너 탈출)
                    apiList.patchRequestEditUserData("ready_minute", inputReadyMinute).enqueue(object :Callback<BasicResponse>{
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){ //200시
                                Toast.makeText(mContext, "준비 시간이 변경되었습니다.",Toast.LENGTH_SHORT)
                                    .show()

                                GlobalData.loginUser = response.body()!!.data.user
                                //TextView에 넣어주기
                                binding.readyMinuteTxt.text = GlobalData.loginUser!!.readyMinute.toString()+"분"
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

        //비밀번호 변경
        binding.changePwLayout.setOnClickListener {

            // 뷰 만든거 변수화
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)
            //뷰 수정
            val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
            inputEdt.visibility = View.GONE//닉네임 변경창 숨기기
            val passwordLayout = customView.findViewById<LinearLayout>(R.id.passwordLayout)
            passwordLayout.visibility = View.VISIBLE //비밀번호 수정 칸 보이기
            //Edt를 변수화
            val currentPwEdt = customView.findViewById<EditText>(R.id.currentPwEdt)
            val newPwEdt = customView.findViewById<EditText>(R.id.newPwEdt)

            //토큰 가져오기
            val token = ContextUtil.getLoginToken(mContext)

            //알럿 창!! 경고창!!
            val alert = AlertDialog.Builder(mContext)
                .setTitle("비밀번호 변경")
                .setMessage("비밀번호 변경을 위해서는 현재 비밀번호가 일치해야 합니다.")
                .setView(customView) // 뷰 넣기
                //확인버튼 선택시
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialogInterface, i ->

                    val currentPw = currentPwEdt.text.toString()
                    val newPw = newPwEdt.text.toString()

                    if(currentPw == newPw){
                        Toast.makeText(mContext,"같은 비밀번호로 변경할 수 없습니다.",Toast.LENGTH_SHORT).show()
                        return@OnClickListener
                    }

                    apiList.patchRequestChangePassword(currentPw, newPw).enqueue(object :Callback<BasicResponse>{
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){ //200시
                                Toast.makeText(mContext, "비밀번호가 변경되었습니다.",Toast.LENGTH_SHORT)
                                    .show()

                                //비밀번호 변경시 토큰이 변경되어 다시 넣어주어야 한다
                                ContextUtil.setLoginToken(mContext, response.body()!!.data.token)
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
        //로그아웃
        binding.logoutLayout.setOnClickListener{
            ContextUtil.setLoginToken(mContext, "")//로그아웃시 토큰 지우기
            GlobalData.loginUser = null //로그아웃시 글로벌 유저 지우기

            val myIntnent = Intent(mContext, LoginActivity::class.java)
            startActivity(myIntnent)
            requireActivity().finishAffinity()
        }
        //친구 목록
        binding.editMyFriendLayout.setOnClickListener {
            val myIntent = Intent(mContext, MyFriendActivity::class.java)
            startActivity(myIntent)
        }

        //장소관리
        binding.editMyPlaceLayout.setOnClickListener {
            val myIntent = Intent(mContext, MyPlaceActivity::class.java)
            startActivity(myIntent)
        }


        //회원탈퇴
        binding.userDeleteLayout.setOnClickListener {

            // 뷰 만든거 변수화
            val customView = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_dialog, null)

            //뷰 수정
            val inputEdt = customView.findViewById<EditText>(R.id.inputEdt)
            inputEdt.maxLines = 2
            inputEdt.setTextSize(Dimension.DP, 35f)
            inputEdt.setHint("회원 탈퇴를 하시려면 '동의' 를 입력해주세요")

            //토큰 가져오기
            val token = ContextUtil.getLoginToken(mContext)

            //알럿 창!! 경고창!!
            val alert = AlertDialog.Builder(mContext)
                .setTitle("회원 탈퇴")
                .setView(customView) // 뷰 넣기
                //확인버튼 선택시
                .setPositiveButton("회원탈퇴", DialogInterface.OnClickListener { dialogInterface, i ->
                    apiList.deleteUser(inputEdt.text.toString()).enqueue(object : Callback<BasicResponse> {
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){
                                Toast.makeText(mContext, "이용해주셔서 감사합니다.",Toast.LENGTH_SHORT).show()
                                ContextUtil.setLoginToken(mContext, "")//로그아웃시 토큰 지우기
                                GlobalData.loginUser = null //로그아웃시 글로벌 유저 지우기

                                val myIntnent = Intent(mContext, LoginActivity::class.java)
                                startActivity(myIntnent)
                                requireActivity().finishAffinity()
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


        Glide.with(mContext)
            .load(GlobalData.loginUser!!.profileImg)
            .into(binding.profileImg)
        binding.nickTxt.text = GlobalData.loginUser!!.nickname
        binding.readyMinuteTxt.text
        binding.readyMinuteTxt.text = "${GlobalData.loginUser!!.readyMinute}분"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) { //
            if (requestCode == REQU_FOR_GALLARY) {

                //data? 가 선택된 사진을 가지고 있다다
                val selectedImgUri = data?.data!! //선택한 사진에 찾아갈 Uri? 결로???

                //Uri 실제 첨부 가능한 파일 형태로 변환해야한다 서버에 넣기 위해
                //File객체를 실제 Path를 통해 만들어준다
                val file = File(URIPathHelper().getPath(mContext, selectedImgUri))

                //우리가 원하는 용량으로 줄이기
                //val fileSize = file.length()//내 사진 Size 확인
                val maxImgSize = 1024000 //내가 원하는 사이즈임

                //원하는 용량이 될때까지 반복해서 용량 압축할거임
                while(true){
                    val bitmap = BitmapFactory.decodeFile(file.path)
                    var quality = 80
                    //file.writeBitmap(bitmap, Bitmap.CompressFormat.JPEG, quality)//80프로로 압축
                    if(file.length() > maxImgSize){
                        quality -= 20 // 80-20 = 60 프로로 압축
                        file.writeBitmap(bitmap, Bitmap.CompressFormat.JPEG, quality)//60프로로 압축
                    }else{
                        break
                    }
                }



                //위 완성된 파일을 Retrofit 에 첨부 가능한 RequestBody 형태로 가공
                val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)

                //파일이 같이 첨부되는 API통신은 Multipart 형태로 모든 데이터를 첨부해야함
                val multipartBody =
                    MultipartBody.Part
                    .createFormData("profile_image", "myProfile.jpg", fileReqBody)


                val token = ContextUtil.getLoginToken(mContext)
                apiList.putRequestUserImg(
                    multipartBody
                ).enqueue(object  : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful){
                            Toast.makeText(mContext, "프로필 사진이 변경되었습니다.", Toast.LENGTH_SHORT).show()
                            GlobalData.loginUser =response.body()!!.data.user

                            //가져온 사진을 넣어준다 - 빠르게 사진만 TextView에 넣어주는 것//(서버에는 X)
                            Glide.with(mContext).load(selectedImgUri).into(binding.profileImg)

                        }else{
                            Toast.makeText(mContext, "프로필 사진 변경 실패", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Log.d("문제", t.toString())
                    }
                })

            }

        }
    }
}