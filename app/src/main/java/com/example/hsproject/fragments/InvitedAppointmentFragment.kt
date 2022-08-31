package com.example.hsproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hsproject.R
import com.example.hsproject.adapters.InvitedAppointmentRecyclerAdapter
import com.example.hsproject.databinding.FragmentInvitedAppointmentBinding
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.InvitedAppointmentsData
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvitedAppointmentFragment : BaseFragment(){

    lateinit var binding : FragmentInvitedAppointmentBinding
    lateinit var mInvitedAppointAdapter : InvitedAppointmentRecyclerAdapter

    val mInvitedAppointList = ArrayList<InvitedAppointmentsData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invited_appointment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
        setValues()
        getAppointmentDataFromServer()
    }

    override fun setEvent() {

    }

    override fun setValues() {
        mInvitedAppointAdapter = InvitedAppointmentRecyclerAdapter(mContext, mInvitedAppointList)
        binding.invitedAppointmentRecyclerView.adapter = mInvitedAppointAdapter
        binding.invitedAppointmentRecyclerView.layoutManager = LinearLayoutManager(mContext)
    }
    fun getAppointmentDataFromServer() {
        apiList.getRequestMyAppointment().enqueue(object : Callback<BasicResponse> {
            override fun onResponse(call: Call<BasicResponse>, response: Response<BasicResponse>) {
                //Log.d("문제", "뭐가 문제일까?")
                if (response.isSuccessful) {
                    mInvitedAppointList.clear()

                    mInvitedAppointList.addAll(response.body()!!.data.invitedAppointments)

                    mInvitedAppointAdapter.notifyDataSetChanged()
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