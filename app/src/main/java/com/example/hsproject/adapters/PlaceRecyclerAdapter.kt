package com.example.hsproject.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.hsproject.MyPlaceActivity
import com.example.hsproject.MyPlaceDetailActivity
import com.example.hsproject.R
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.PlaceData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlaceRecyclerAdapter(
    val mContext: Context, val mList: List<PlaceData>
) : RecyclerView.Adapter<PlaceRecyclerAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: PlaceData) {
            val myPlaceTxt = itemView.findViewById<TextView>(R.id.myPlaceTxt)
            val primaryTxt = itemView.findViewById<TextView>(R.id.primaryTxt)

            myPlaceTxt.text = item.name
            if (item.isPrimary) {
                primaryTxt.visibility = View.VISIBLE
            }

            itemView.setOnClickListener {
                val myIntent = Intent(mContext, MyPlaceDetailActivity::class.java)
                myIntent.putExtra("placeData", item)
                mContext.startActivity(myIntent)
            }


            //
            itemView.setOnClickListener {

                //알럿 창!! 경고창!!
                val alert = AlertDialog.Builder(mContext)
                    .setTitle("기본 출발장소 변경 / 삭제")
                    //확인버튼 선택시
                    .setPositiveButton("변경", DialogInterface.OnClickListener { dialogInterface, i ->
                        //어답터에서는 액티비티에서 받아와야 서버 연결 가능
                        (mContext as MyPlaceActivity).apiList
                            .patchRequestEditPlace(
                                item.id
                            )
                            .enqueue(object : Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    Toast.makeText(
                                        mContext,
                                        "기본 출발 장소가 변경되었습니다",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    mContext.getPlaceListFromServer()//어답터를 포함한 액티비티의 정보 새로고침
                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                                }

                            })
                    })
                    //삭제 버튼 클릭시
                    .setNegativeButton("삭제", DialogInterface.OnClickListener { dialogInterface, i ->
                        (mContext as MyPlaceActivity).apiList
                            .deleteRequestEditPlace(item.id)
                            .enqueue(object : Callback<BasicResponse> {
                                override fun onResponse(
                                    call: Call<BasicResponse>,
                                    response: Response<BasicResponse>
                                ) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(
                                            mContext,
                                            "해당 출발 장소가 삭제되었습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        mContext.getPlaceListFromServer()//어답터를 포함한 액티비티의 정보 새로고침
                                    }
                                }

                                override fun onFailure(call: Call<BasicResponse>, t: Throwable) {

                                }
                            })


                    })
                    .show()

/*                */
/*                */
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