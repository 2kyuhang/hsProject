package com.example.hsproject.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hsproject.MyFriendActivity
import com.example.hsproject.R
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.UserData
import com.example.hsproject.fragments.MyFriendFragment
import com.example.hsproject.fragments.RequestedFriendFragment
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


            Glide.with(mContext).load(item.profileImg).into(profileImg)
            nickTxt.text = item.nickname
            when (item.provider) {
                "facebook" -> {
                    emailTxt.text = "페북 로그인"
                }
                "kakao" -> {
                    emailTxt.text = "카카오 로그인"
                }
                "naver" -> {
                    emailTxt.text = "네이버 로그인"
                }
                else -> {
                    emailTxt.text = item.email
                }
            }

            //하나의 어답터로 돌려쓰기 위해 수락 거절 등등을 상황에 따라 보여주고 숨긴다
            if (type == "request") {
                buttonLayout.visibility = View.VISIBLE
            }

            val ocl = object : View.OnClickListener{
                override fun onClick(p0: View?) { //View는 하나하나의 뷰?
                    val type = p0!!.tag.toString()//그리고 그안에 태그값을 가져온다?

                    //Log.d("문제", "${type} ${item.id}")

                    (mContext as MyFriendActivity).apiList.putRequestAddFriend(
                        item.id, type
                    ).enqueue(object : Callback<BasicResponse>{
                        override fun onResponse(
                            call: Call<BasicResponse>,
                            response: Response<BasicResponse>
                        ) {
                            if(response.isSuccessful){
                                Toast.makeText(mContext, "${item.nickname}님의 친구 추가 요청을 ${type}하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<BasicResponse>, t: Throwable) {}
                    })
                    //요청 수락시 // 목록 갱신
                    //여기 어답터를 담고있는 프레그먼트의 => 그 프레그먼트의 부모인 ViewPager2에서
                    // tag("f"+index)  값을 통해 프레그 먼트를 찾아
                    //갱신을 해주는 과정!!!!!
                    (mContext.supportFragmentManager
                        .findFragmentByTag("f1") as RequestedFriendFragment)
                        .getRequestFriendListFromServer()

                    //친구 목록도 새로고침
                    (mContext.supportFragmentManager
                        .findFragmentByTag("f0") as MyFriendFragment)
                        .getFriendDataFromServer()

                }
            }

            negativeBtn.setOnClickListener(ocl)
            positiveBtn.setOnClickListener(ocl)


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