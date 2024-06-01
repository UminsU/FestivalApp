package com.example.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class FragmentFal : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_fal, container, false)

        val imageViewFal1 = view.findViewById<ImageView>(R.id.fal_image1)
        imageViewFal1.setOnClickListener {
            val festivalId = "9kCzzEkn4pgBrUU69RFC" // 파이어베이스 문서의 ID
            val intent = Intent(requireContext(), ReadBoardActivity::class.java)
            intent.putExtra("festivalId", festivalId)
            intent.putExtra("imageResId", R.drawable.fes1)
            startActivity(intent)
        }

        val imageViewFal2 = view.findViewById<ImageView>(R.id.fal_image2)
        imageViewFal2.setOnClickListener {
            val festivalId = "RJfMKjgiXG2qnSZgePPf" // 파이어베이스 문서의 ID
            val intent = Intent(requireContext(), ReadBoardActivity::class.java)
            intent.putExtra("festivalId", festivalId)
            intent.putExtra("imageResId", R.drawable.fes2)
            startActivity(intent)
        }

        return view
    }
}
