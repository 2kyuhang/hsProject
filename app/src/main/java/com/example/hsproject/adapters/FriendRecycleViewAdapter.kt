package com.example.hsproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hsproject.MyFriendActivity
import com.example.hsproject.R
import com.example.hsproject.datas.UserData
import com.example.hsproject.fragments.RequestedFriendFragment

class FriendRecycleViewAdapter(
    val mContext: Context, val mList: List<UserData>, var type: String
) : RecyclerView.Adapter<FriendRecycleViewAdapter.MyViewHolder>() {



    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: UserData) {

            val profileImg = itemView.findViewById<ImageView>(R.id.profileImg)
            val nickTxt = itemView.findViewById<TextView>(R.id.nickTxt)
            val emailTxt = itemView.findViewById<TextView>(R.id.emailTxt)
            //수락 거절
            val buttonLayout = itemView.findViewById<LinearLayout>(R.id.buttonLayout)
            val positiveBtn = itemView.findViewById<Button>(R.id.positiveBtn)
            val negativeBtn = itemView.findViewById<Button>(R.id.negativeBtn)

            //하나의 어답터로 돌려쓰기 위해 수락 거절 등등을 상황에 따라 보여주고 숨긴다
            if (type == "request") {
                buttonLayout.visibility = View.VISIBLE

                //그렇다보니 그냥 여기서 이벤트 처리해도 괜찮다//어차피 false면 안보이기 때문
                positiveBtn.setOnClickListener {
                    (mContext as MyFriendActivity)
                }
                negativeBtn.setOnClickListener { }
            }

            Glide.with(mContext).load(item.profileImg).into(profileImg)
            nickTxt.text = item.nickname
            when (item.provider) {
                "facebook" -> {
                    emailTxt.text = "페북 로그인"
                }
                "kakao" -> {
                    emailTxt.text = "카카오 로그인"
                }
                else -> {
                    emailTxt.text = "네이버 로그인"
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.friend_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}