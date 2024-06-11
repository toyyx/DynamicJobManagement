package com.example.dynamicjobmanagement.view.Adapter.CourseCenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.CourseBaseInformationFragment
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.CourseJobFragment
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.CourseMemberFragment


class CourseInfoViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragmentList: List<Fragment> = listOf(
        CourseBaseInformationFragment(),
        CourseJobFragment(),
        CourseMemberFragment()
    )
    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }

    fun getFragment(position: Int): Fragment {
        return fragmentList[position]
    }



}
