package com.example.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfileAdapter(val profileList: ArrayList<Profiles>) : RecyclerView.Adapter<ProfileAdapter.CustomViewholder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.CustomViewholder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_list, parent, false)
        return CustomViewholder(view)
    }

    override fun getItemCount(): Int {
        return  profileList.size
    }

    override fun onBindViewHolder(holder: ProfileAdapter.CustomViewholder, position: Int) {
        holder.board1.setImageResource(profileList.get(position).board1)
        holder.board2.setImageResource(profileList.get(position).board2)
        holder.text1.text = profileList.get(position).name1
        holder.text2.text = profileList.get(position).name2
    }

    class CustomViewholder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val board1 = itemView.findViewById<ImageView>(R.id.image_board_1) // 이미지 1
        val board2 = itemView.findViewById<ImageView>(R.id.image_board_2) // 이미지 2
        val text1 = itemView.findViewById<TextView>(R.id.text_board_1) // 텍스트 1
        val text2 = itemView.findViewById<TextView>(R.id.text_board_2) // 텍스트 2

    }
}