package com.example.hsproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsproject.R
import com.example.hsproject.datas.InvitedAppointmentsData
import java.text.SimpleDateFormat

class InvitedAppointmentRecyclerAdapter (
    val mContext: Context, val mList : List<InvitedAppointmentsData>
): RecyclerView.Adapter<InvitedAppointmentRecyclerAdapter.MyViewHolder>(){

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item : InvitedAppointmentsData){
            val titleTxt = itemView.findViewById<TextView>(R.id.titleTxt)
            val dateTimeTxt = itemView.findViewById<TextView>(R.id.dateTimeTxt)
            val placeTxt = itemView.findViewById<TextView>(R.id.placeTxt)
            val memberTxt = itemView.findViewById<TextView>(R.id.memberTxt)

            val formatter = SimpleDateFormat("M/dd a h:ss")
            titleTxt.text = item.title+"("+item.user.nickname+" 님이 초대)"
            dateTimeTxt.text = formatter.format(item.datetime)
            placeTxt.text = "약속 장소 : ${item.place}"
            memberTxt.text = "참여 인원 : ${item.invitedFriends.size}명"

            itemView.setOnClickListener {
/*                var myIntent = Intent(mContext, AppointDetailActivity::class.java)
                myIntent.putExtra("invitedAppointmentData", item)
                mContext.startActivity(myIntent)*/
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