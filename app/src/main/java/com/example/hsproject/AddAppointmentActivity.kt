package com.example.hsproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hsproject.databinding.ActivityAddAppointmentBinding
import com.example.hsproject.datas.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AddAppointmentActivity : BaseActivity() {

    lateinit var binding : ActivityAddAppointmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_appointment)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        binding.saveBtn.setOnClickListener {
            //약속 정함?
            val inputTitle = binding.titleEdt.toString()
            if(inputTitle.isBlank()){
                return@setOnClickListener
            }
            //약속 일시
            if(binding.dataTxt.text == "날짜 선택"){
                Toast.makeText(mContext, "날짜를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //시간 선택
            if(binding.timeTxt.text == "시간 선택"){
                Toast.makeText(mContext, "시간을 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //지난날을 선택했나?
            /*if(){
                Toast.makeText(mContext, "", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }*/
            //약속 장소명
            val inputPlaceName = binding.placeNameEdt.text.toString()
            if(inputPlaceName.isBlank()){
                Toast.makeText(mContext, "약속 장소명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            //실제 도착 지점을 선택했는가? 네이버 지도
/*            apiList.postRequestAddAppointment(
                inputTitle, inputPlaceName,
            ).enqueue(object : Callback<BasicResponse>{
                override fun onResponse(
                    call: Call<BasicResponse>,
                    response: Response<BasicResponse>
                ) {

                }

                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                }

            })*/

        }
    }

    override fun setValues() {
        titleTxt.text = "새 약속 만들기"
        backIcon.visibility = View.VISIBLE
    }
}