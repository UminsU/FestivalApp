package com.example.app

import ImageSliderAdapter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.app.databinding.FragmentHomeBinding
import java.util.*

class FragmentHome : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val imageList1 = listOf(R.drawable.fes1, R.drawable.fes2, R.drawable.fes3, R.drawable.fes4)
    private val nameList1 = listOf("남산 벚꽃축제", "한강공원 벚꽃축제", "남양주 해바라기축제", "서울 불꽃축제")
    private val imageList2 = listOf(R.drawable.fes5, R.drawable.fes6, R.drawable.fes7, R.drawable.fes8)
    private val nameList2 = listOf("경복궁 문화제", "노원 야시장", "별내 문화예술콘서트", "화성 행군축제")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter1 = ImageSliderAdapter(imageList1, nameList1)
        binding.monthFesPage.adapter = adapter1
        binding.monthFesName.text = nameList1[0]

        val adapter2 = ImageSliderAdapter(imageList2, nameList2)
        binding.placeFesPage.adapter = adapter2
        binding.placeFesName.text = nameList2[0]

        binding.monthFesPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.monthFesName.text = nameList1[position]
            }
        })

        binding.placeFesPage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.placeFesName.text = nameList2[position]
            }
        })

        // 자동 페이지 전환 설정
        val handler = Handler(Looper.getMainLooper())
        val update1 = Runnable {
            var currentPage = binding.monthFesPage.currentItem
            currentPage = (currentPage + 1) % imageList1.size
            binding.monthFesPage.setCurrentItem(currentPage, true)
        }
        val update2 = Runnable {
            var currentPage = binding.placeFesPage.currentItem
            currentPage = (currentPage + 1) % imageList2.size
            binding.placeFesPage.setCurrentItem(currentPage, true)
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(update1)
                handler.post(update2)
            }
        }, 3000, 3000)
    }
}
