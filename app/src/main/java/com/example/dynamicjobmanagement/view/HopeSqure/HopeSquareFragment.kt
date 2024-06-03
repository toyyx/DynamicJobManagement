package com.example.dynamicjobmanagement.view.HopeSqure

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.view.Adapter.HopeSquare.HopeSquareViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class HopeSquareFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_hope_square, container, false)

        val viewPager = view.findViewById<ViewPager2>(R.id.hopeSquare_ViewPager2)
        val tabLayout = view.findViewById<TabLayout>(R.id.hopeSquare_TabLayout)

        val adapter = HopeSquareViewPagerAdapter(this)
        viewPager.adapter = adapter

        // 将TabLayout与ViewPager2关联
        TabLayoutMediator(tabLayout, viewPager) {
                tab, position ->
//            // 根据position设置Tab的标题
//            tab.text = when (position) {
//                0 -> "基本信息"
//                1 -> "作业"
//                2 -> "成员"
//                else -> throw IllegalArgumentException("Invalid position")
//            }
        }.attach()
        return view
    }

    companion object {
        fun newInstance(): HopeSquareFragment {
            return HopeSquareFragment()
        }
    }
}