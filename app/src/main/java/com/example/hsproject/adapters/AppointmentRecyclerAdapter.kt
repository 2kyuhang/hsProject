package com.example.hsproject.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsproject.AppointDetailActivity
import com.example.hsproject.R
import com.example.hsproject.datas.AppointmentData
import java.text.SimpleDateFormat

class AppointmentRecyclerAdapter(
    val mContext: Context, val mList : List<AppointmentData>
) : RecyclerView.Adapter<AppointmentRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item : AppointmentData) {
            val titleTxt = itemView.findViewById<TextView>(R.id.titleTxt)
            val dateTimeTxt = itemView.findViewById<TextView>(R.id.dateTimeTxt)
            val placeTxt = itemView.findViewById<TextView>(R.id.placeTxt)
            val memberTxt = itemView.findViewById<TextView>(R.id.memberTxt)
//
//            val sdf = SimpleDateFormat("yyyy-MM-dd HH:ss")
//            val datetime = sdf.parse(item.datetime)

            val formatter = SimpleDateFormat("M/dd a h:ss")
            titleTxt.text = item.title
            dateTimeTxt.text = formatter.format(item.datetime)
            placeTxt.text = "약속 장소 : ${item.place}"
            memberTxt.text = "참여 인원 : ${item.invitedFriends.size}명"

            itemView.setOnClickListener {
                var myIntent = Intent(mContext, AppointDetailActivity::class.java)
                //인텐트로 약속상세에 appointmentData 정보 주기
                //myIntent.putExtra("appointmentData",item)
                myIntent.putExtra("appointmentData",item)

                //myIntent.putExtra("appointmentIdData",item.id)
                //Log.d("문제", "${item.id}")
                mContext.startActivity(myIntent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.appoint_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}