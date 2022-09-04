package com.example.hsproject.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBindings
import com.example.hsproject.AddAppointmentActivity
import com.example.hsproject.R
import com.example.hsproject.adapters.AppointmentRecyclerAdapter
import com.example.hsproject.databinding.FragmentMyAppointmentBinding
import com.example.hsproject.datas.AppointmentData
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.utils.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyAppointmentFragment : BaseFragment(){

    lateinit var binding : FragmentMyAppointmentBinding

    lateinit var mAppointAdapter : AppointmentRecyclerAdapter
    val mAppointList = ArrayList<AppointmentData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_appointment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setEvent()
        setValues()
    }
    override fun onResume() {
        super.onResume()
        getAppointmentDataFromServer()
    }

    override fun setEvent() {
        binding.addAppointBtn.setOnClickListener {
            val myIntent = Intent(mContext, AddAppointmentActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        mAppointAdapter = AppointmentRecyclerAdapter(mContext, mAppointList)
        binding.appointmentRecyclerView.adapter = mAppointAdapter
        binding.appointmentRecyclerView.layoutManager = LinearLayoutManager(mContext)

    }

    fun getAppointmentDataFromServer() {
        apiList.getRequestMyAppointment().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                //Log.d("문제", "뭐가 문제일까?")
                if (response.isSuccessful) {
                    mAppointList.clear()

                    mAppointList.addAll(response.body()!!.data.appointments)

                    Log.d("문제 리스트 사이즈","${mAppointList.size}")
                    if(mAppointList.size < 1){
                        val addBtn = binding.addAppointBtn
                        addBtn.visibility = View.VISIBLE
                    }

                    mAppointAdapter.notifyDataSetChanged()
                }
                else {
                    val errorBodyStr = response.errorBody()!!.string()
                    val jsonObj = JSONObject(errorBodyStr)

                    val message = jsonObj.getString("message")
                    Log.d("파싱 에러", message)
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                Log.d("응답 에러", t.toString())
            }
        })
    }
}