package com.example.hsproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hsproject.R
import com.example.hsproject.datas.PlaceData

class PlaceRecyclerAdapter(
    val mContext : Context, val mList : List<PlaceData>
) : RecyclerView.Adapter<PlaceRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PlaceData){
            val myPlaceTxt = itemView.findViewById<TextView>(R.id.myPlaceTxt)
            val primaryTxt = itemView.findViewById<TextView>(R.id.primaryTxt)

            myPlaceTxt.text = item.name
            if(item.isPrimary){
                primaryTxt.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val row = LayoutInflater.from(mContext).inflate(R.layout.place_list_item, parent, false)
        return MyViewHolder(row)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

}