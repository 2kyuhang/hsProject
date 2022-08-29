package com.example.hsproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsproject.adapters.ChattingRecyclerAdapter
import com.example.hsproject.databinding.ActivityChattingBinding
import com.example.hsproject.datas.AppointmentData
import com.example.hsproject.datas.ChattingData
import com.example.hsproject.utils.ContextUtil
import com.example.hsproject.utils.GlobalData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChattingActivity : BaseActivity() {

    lateinit var binding : ActivityChattingBinding

    val database = FirebaseDatabase.getInstance("https://hsproject-df016-default-rtdb.asia-southeast1.firebasedatabase.app/")

    lateinit var mAdapter : ChattingRecyclerAdapter
    val mList = ArrayList<ChattingData>()

    lateinit var appointmentData : AppointmentData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this ,R.layout.activity_chatting)
        setValues()
        setupEvents()
    }


    override fun setupEvents() {
        binding.sendBtn.setOnClickListener {
//            서버에 데이터 기록
            val inputContent = "${GlobalData.loginUser!!.nickname} : "+binding.contentEdt.text.toString()
            val now = Calendar.getInstance()

            val sdf = SimpleDateFormat("a h:mm")
            val nowStr = sdf.format(now.time)
            val deviceToken = ContextUtil.getDeviceToken(mContext)


            Log.d("현재 시간", nowStr)

//            맵 구조 활용 한번에 서버에 전송
            val inputMap = HashMap<String, String>()

            //유저 명도 인텐트로 받아와서 넣을거다
            inputMap["content"] = inputContent
            inputMap["date"] = nowStr
            inputMap["deviceToken"] = deviceToken

            //아마 message 부분에 약속고유ID 넣고 아래도 고유ID 다른방 처럼 생길듯?
            database.getReference("${appointmentData.id}message").setValue(inputMap)

            binding.contentEdt.setText("")
        }
        //항목의 데이터 변경 감지
        database.getReference("${appointmentData.id}message")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("message 항목 값", snapshot.value.toString())//message 모든 값이 들어 있다
                    //binding.contentTxt.text = snapshot.child("content").value.toString()

                    mList.add(0,
                        ChattingData(
                            snapshot.child("content").value.toString(),
                            snapshot.child("date").value.toString(),
                            snapshot.child("deviceToken").value.toString()
                        )
                    )
                    mAdapter.notifyDataSetChanged()//새로고침
                }
                //데이터 취소시 어떻할거냐?
                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    override fun setValues() {
        backIcon.visibility = View.VISIBLE

        mAdapter = ChattingRecyclerAdapter(mContext,mList)
        binding.chattingRecyclerView.adapter=mAdapter
        binding.chattingRecyclerView.layoutManager = LinearLayoutManager(mContext)

        //약속 디테일 페이지에서 정보 받아오기
        appointmentData = intent.getSerializableExtra("appointmentData") as AppointmentData

    }
}