package com.example.hsproject.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.hsproject.R
import com.example.hsproject.databinding.FragmentSettingBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.utils.ContextUtil
import com.example.hsproject.utils.GlobalData
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
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .check()

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

        if (requestCode == Activity.RESULT_OK) {
            if (requestCode == REQU_FOR_GALLARY) {

                //data? 가 선택된 사진을 가지고 있다다
                val selectedImgUri = data?.data!! //선택한 사진에 찾아갈 Uri? 결로???

                //Uri 실제 첨부 가능한 파일 형태로 변환해야한다 서버에 넣기 위해
                //File객체를 실제 Path를 통해 만들어준다
                val file = File(URIPathHelper().getPath(mContext, selectedImgUri))

                //위 완성된 파일을 Retrofit 에 첨부 가능한 RequestBody 형태로 가공
                val fileReqBody = RequestBody.create(MediaType.get("image/*"), file)

                //파일이 같이 첨부되는 API통신은 Multipart 형태로 모든 데이터를 첨부해야함
                val multipartBody =
                    MultipartBody.Part.
                        createFormData("profile_image", "myProfile.jpg", fileReqBody)


                val token = ContextUtil.getLoginToken(mContext)
                apiList.putRequestUserImg(
                    token, multipartBody
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

                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                    }
                })

            }

        }
    }
}