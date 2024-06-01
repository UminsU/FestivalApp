package com.example.app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class ProfileAdapter(var profileList: ArrayList<Profiles>) : RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {
    private var filteredList = ArrayList<Profiles>()

    init {
        filteredList.addAll(profileList)
    }

    // 이곳에서 festivalId를 정의합니다.
    private val festivalId1 = "9kCzzEkn4pgBrUU69RFC"

    fun filter(text: String) {
        filteredList.clear()
        if (text.isEmpty()) {
            filteredList.addAll(profileList)
        } else {
            val searchText = text.toLowerCase(Locale.getDefault())
            for (profile in profileList) {
                if (profile.name1.toLowerCase(Locale.getDefault()).contains(searchText)) {
                    filteredList.add(profile)
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.board_list, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val profile = filteredList[position]
        holder.board1.setImageResource(profile.board1)
        holder.text1.text = profile.name1
    }

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val board1: ImageView = itemView.findViewById(R.id.image_board)
        val text1: TextView = itemView.findViewById(R.id.text_board)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val profile = filteredList[position]
                    val intent = Intent(itemView.context, ReadBoardActivity::class.java)
                    intent.putExtra("festivalId", profile.festivalId) // 고유한 festivalId 사용
                    intent.putExtra("imageResId", profile.imageResId) // 고유한 imageResId 사용
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
