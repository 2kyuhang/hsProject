package com.example.hsproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.hsproject.R
import com.example.hsproject.databinding.ChattingListItemBinding
import com.example.hsproject.datas.ChattingData
import com.example.hsproject.utils.ContextUtil

class ChattingRecyclerAdapter(
    val mContext : Context, val mList : List<ChattingData>)
    : RecyclerView.Adapter<ChattingRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding : ChattingListItemBinding)
        :RecyclerView.ViewHolder(binding.root){
            fun bind(item : ChattingData){
                binding.contentTxt.text = item.content
                binding.dateTxt.text = item.date
                if(item.deviceToken == ContextUtil.getDeviceToken(mContext)){
                        binding.contentTxt.setTextColor(ContextCompat.getColorStateList(mContext,R.color.teal_200))
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ChattingListItemBinding.inflate(LayoutInflater.from(mContext), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position ])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}