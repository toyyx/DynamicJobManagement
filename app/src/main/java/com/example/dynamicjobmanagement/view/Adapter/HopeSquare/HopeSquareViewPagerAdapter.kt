package com.example.dynamicjobmanagement.view.Adapter.HopeSquare

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.MySquareFragment
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.SquareCenterFragment

class HopeSquareViewPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SquareCenterFragment()
            1 -> MySquareFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}