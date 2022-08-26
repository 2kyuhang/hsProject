package com.example.hsproject.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hsproject.AddAppointmentActivity
import com.example.hsproject.R
import com.example.hsproject.databinding.FragmentMyAppointmentBinding


class MyAppointmentFragment : BaseFragment(){

    lateinit var binding : FragmentMyAppointmentBinding

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

    override fun setEvent() {
        addBtn.setOnClickListener {
            val myIntent = Intent(mContext, AddAppointmentActivity::class.java)
            startActivity(myIntent)
        }
    }

    override fun setValues() {
        addBtn.visibility = View.VISIBLE
    }

}