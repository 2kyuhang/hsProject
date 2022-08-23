package com.example.hsproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.hsproject.R
import com.example.hsproject.databinding.FragmentMyFriendsBinding
import com.example.hsproject.databinding.FragmentRequestedFriendsBinding

class RequestedFriendFragment  : BaseFragment(){

    lateinit var binding : FragmentRequestedFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_requested_friends, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEvent()
        setValues()
    }

    override fun setEvent() {

    }

    override fun setValues() {

    }


}