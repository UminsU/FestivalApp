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
import com.example.app.databinding.FragmentBoardBinding

class FragmentBoard : Fragment() {
    private lateinit var binding: FragmentBoardBinding
    private lateinit var profileAdapter: ProfileAdapter
    private var profileList = arrayListOf<Profiles>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 초기 프로필 리스트 설정
        profileList.addAll(
            listOf(
                Profiles(R.drawable.fes1, "남산벚꽃축제","9kCzzEkn4pgBrUU69RFC", R.drawable.fes1),
                Profiles(R.drawable.fes2, "한강공원벚꽃축제","RJfMKjgiXG2qnSZgePPf)",R.drawable.fes2),
                Profiles(R.drawable.fes3, "남양주해바라기축제","9kCzzEkn4pgBrUU69RFC", R.drawable.fes3),
                Profiles(R.drawable.fes4, "한강불꽃축제","9kCzzEkn4pgBrUU69RFC", R.drawable.fes4),
                Profiles(R.drawable.fes5, "경복궁문화체험","9kCzzEkn4pgBrUU69RFC", R.drawable.fes5),
                Profiles(R.drawable.fes6, "노원야시장","9kCzzEkn4pgBrUU69RFC", R.drawable.fes6),
                Profiles(R.drawable.fes7, "별내문화예술콘서트","9kCzzEkn4pgBrUU69RFC", R.drawable.fes7),
                Profiles(R.drawable.fes8, "화성행군축제","9kCzzEkn4pgBrUU69RFC", R.drawable.fes8)
            )
        )

        // RecyclerView 설정
        profileAdapter = ProfileAdapter(profileList)
        binding.rvProfile.layoutManager = LinearLayoutManager(activity)
        binding.rvProfile.adapter = profileAdapter

        // 키워드 버튼 및 레이아웃 설정
        val keywordButton = binding.keywardButton

        keywordButton.setOnClickListener {
            if (binding.keywardLayout.visibility == View.VISIBLE) {
                binding.keywardLayout.visibility = View.GONE
            } else {
                binding.keywardLayout.visibility = View.VISIBLE
            }
        }
        val button1 = binding.buttonKeyword1
        val button2 = binding.buttonKeyword2

        button1.setOnClickListener {
            // 버튼 1을 클릭했을 때
            profileAdapter.filter("fes1")
        }

        button2.setOnClickListener {
            // 버튼 2를 클릭했을 때
            profileAdapter.filter("fes2")
        }

        // 키워드 버튼 클릭 이벤트 처리
        val keywordButtons = listOf(
            binding.buttonKeyword1,
            binding.buttonKeyword2,
            binding.buttonKeyword3,
            binding.buttonKeyword4,
            binding.buttonKeyword5,
            binding.buttonKeyword6,
            binding.buttonKeyword7,
            binding.buttonKeyword8,
            binding.buttonKeyword9,
            binding.buttonKeyword10,
            binding.buttonKeyword11,
            binding.buttonKeyword12
        )

        for (button in keywordButtons) {
            button.setOnClickListener {
                val keyword = button.text.toString()
                profileAdapter.filter(keyword)
            }
        }

        // SearchView 설정
        binding.search.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { profileAdapter.filter(it) }
                return true
            }
        })
    }
}
