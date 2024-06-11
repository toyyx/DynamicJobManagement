package com.example.dynamicjobmanagement.view.Adapter.HopeSquare

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.CourseBaseInformationFragment
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.CourseJobFragment
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.CourseMemberFragment
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.MySquareFragment
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.SquareCenterFragment

class HopeSquareViewPagerAdapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentList: List<Fragment> = listOf(
        SquareCenterFragment(),
        MySquareFragment()
    )
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}