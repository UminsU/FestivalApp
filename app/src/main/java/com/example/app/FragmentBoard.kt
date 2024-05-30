package com.example.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FragmentBoard : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_board, container, false)

        // RecyclerView와 그 어댑터를 수동 설정
        val profileList = arrayListOf(
            Profiles(R.drawable.myp02, R.drawable.myp02, "ㅇㅇ축제", "ㅁㅁ축제"),
            Profiles(R.drawable.fal02, R.drawable.fal02, "ㅇㅇ축제", "ㅁㅁ축제"),
            Profiles(R.drawable.myp02, R.drawable.myp02, "ㅇㅇ축제", "ㅁㅁ축제"),
            Profiles(R.drawable.fal02, R.drawable.fal02, "ㅇㅇ축제", "ㅁㅁ축제"),
            Profiles(R.drawable.myp02, R.drawable.myp02, "ㅇㅇ축제", "ㅁㅁ축제"),
            Profiles(R.drawable.fal02, R.drawable.fal02, "ㅇㅇ축제", "ㅁㅁ축제"),
            Profiles(R.drawable.myp02, R.drawable.myp02, "ㅇㅇ축제", "ㅁㅁ축제"),
            Profiles(R.drawable.fal02, R.drawable.fal02, "ㅇㅇ축제", "ㅁㅁ축제"),
            Profiles(R.drawable.myp02, R.drawable.myp02, "ㅇㅇ축제", "ㅁㅁ축제")
        )

        val rvProfile = view.findViewById<RecyclerView>(R.id.rv_profile)
        rvProfile.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvProfile.setHasFixedSize(true)
        rvProfile.adapter = ProfileAdapter(profileList)

        val keywordButton = view.findViewById<Button>(R.id.keyward_button)
        val keywordLayout = view.findViewById<LinearLayout>(R.id.keyward_layout)
        val boardTextView = view.findViewById<TextView>(R.id.board_textView)

        keywordButton.setOnClickListener {
            if (keywordLayout.visibility == View.VISIBLE) {
                keywordLayout.visibility = View.GONE
            } else {
                keywordLayout.visibility = View.VISIBLE
            }
        }

        val keywordButtons = listOf(
            R.id.button_keyword_1,
            R.id.button_keyword_2,
            R.id.button_keyword_3,
            R.id.button_keyword_4,
            R.id.button_keyword_5,
            R.id.button_keyword_6,
            R.id.button_keyword_7,
            R.id.button_keyword_8,
            R.id.button_keyword_9,
            R.id.button_keyword_10,
            R.id.button_keyword_11,
            R.id.button_keyword_12
        )

        for (buttonId in keywordButtons) {
            val button = view.findViewById<Button>(buttonId)
            button.setOnClickListener {
                val text = button.text.toString()
                val currentText = boardTextView.text.toString()
                if (!currentText.contains("#$text")) {
                    boardTextView.text = "$currentText #$text"
                }
            }
        }
        return view
    }
}
