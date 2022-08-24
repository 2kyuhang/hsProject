package com.example.hsproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hsproject.R
import com.example.hsproject.datas.UserData

class FriendRecycleViewAdapter(val mContext : Context, val mList : List<UserData>
): RecyclerView.Adapter<FriendRecycleViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(item : UserData){

            val profileImg = itemView.findViewById<ImageView>(R.id.profileImg)
            val nickTxt = itemView.findViewById<TextView>(R.id.nickTxt)
            val emailTxt = itemView.findViewById<TextView>(R.id.emailTxt)


            Glide.with(mContext).load(item.profileImg).into(profileImg)
            nickTxt.text = item.nickname
            when(item.provider){
                "facebook" -> {emailTxt.text = "페북 로그인"}
                "kakao" -> {emailTxt.text = "카카오 로그인"}
                else ->  {emailTxt.text = "네이버 로그인"}
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item, parent,false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}