package com.example.hsproject.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hsproject.R
import com.example.hsproject.SearchUserActivity
import com.example.hsproject.api.APIList
import com.example.hsproject.api.ServerAPI
import com.example.hsproject.datas.BasicResponse
import com.example.hsproject.datas.UserData
import com.example.hsproject.utils.ContextUtil
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SerchUserRecycleViewAdapter(val mContext : Context, val mList : List<UserData>
): RecyclerView.Adapter<SerchUserRecycleViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view){
        fun bind(item : UserData){

            val profileImg = itemView.findViewById<ImageView>(R.id.profileImg)
            val nickTxt = itemView.findViewById<TextView>(R.id.nickTxt)
            val emailTxt = itemView.findViewById<TextView>(R.id.emailTxt)
            val addFriendBtn = itemView.findViewById<Button>(R.id.addFriendBtn)

            addFriendBtn.visibility = View.VISIBLE

            Glide.with(mContext).load(item.profileImg).into(profileImg)
            nickTxt.text = item.nickname
            when(item.provider){
                "facebook" -> {emailTxt.text = "페북 로그인"}
                "kakao" -> {emailTxt.text = "카카오 로그인"}
                "naver" ->  {emailTxt.text = "네이버 로그인"}
                else -> {emailTxt.text = item.email}
            }

            addFriendBtn.setOnClickListener {
                //여기는 API객체를 가져올 수 없다
                //그렇기에 새로 객체화 해서 사용하기
/*                val retrofit =ServerAPI.getRetrofit()
                val apiList = retrofit.create(APIList::class.java)*/

                //2번째 방법
                //SearchUserActivity 를 통해서 가져오기  //단점 여기서 mContext 를 사용하면 그건 다 SearchUserActivity이다...
                val apiList = (mContext as SearchUserActivity).apiList

                val token = ContextUtil.getLoginToken(mContext)

                apiList.postRequestAddFriend(token, item.id).enqueue(object : Callback<BasicResponse>{
                    override fun onResponse(
                        call: Call<BasicResponse>,
                        response: Response<BasicResponse>
                    ) {
                        if(response.isSuccessful){//200성공시
                            Toast.makeText(mContext,"${item.nickname}님께 친구요청을 보냈습니다.", Toast.LENGTH_SHORT).show()
                        }else{
                            val errorBodyStr = response.errorBody()!!.string()
                            val jsonObj = JSONObject(errorBodyStr)
                            val code = jsonObj.getInt("code")
                            val message = jsonObj.getString("message")
                            if(code == 400){
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                        Log.d("문제", t.toString())
                    }

                })
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