package com.example.dynamicjobmanagement.view.Adapter.CourseCenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.ViewPager2.CourseBaseInformationFragment
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.ViewPager2.CourseJobFragment
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.ViewPager2.CourseMemberFragment


class CourseInfoViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CourseBaseInformationFragment()
            1 -> CourseJobFragment()
            2 -> CourseMemberFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
