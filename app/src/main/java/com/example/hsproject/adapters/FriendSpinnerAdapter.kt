package com.example.hsproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.hsproject.R
import com.example.hsproject.datas.UserData

class FriendSpinnerAdapter(val mContext : Context,val resId : Int,val mList : List<UserData>
) : ArrayAdapter<UserData>(mContext, resId, mList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView
        if(tempRow == null){
            tempRow = LayoutInflater.from(mContext).inflate(resId, null)
        }
        val row = tempRow!!

        val profileImg = row.findViewById<ImageView>(R.id.profileImg)
        val nickTxt =row.findViewById<TextView>(R.id.nickTxt)
        val emailTxt = row.findViewById<TextView>(R.id.emailTxt)

        Glide.with(mContext).load(mList[position].profileImg).into(profileImg)
        nickTxt.text = mList[position].nickname
        emailTxt.text = mList[position].email

        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

}
