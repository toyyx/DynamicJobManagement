package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Course
import com.example.dynamicjobmanagement.view.Adapter.CourseCenter.CourseInfoViewPagerAdapter
import com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.CourseInfoViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CourseInformationActivity : AppCompatActivity() {
    private val viewModel: CourseInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_information)

        // 接收传递的数据
        val course = intent.getSerializableExtra("course") as? Course
        viewModel.course.value=course

        val viewPager = findViewById<ViewPager2>(R.id.courseInfo_ViewPager2)
        val tabLayout = findViewById<TabLayout>(R.id.courseInfo_TabLayout)

        val adapter = CourseInfoViewPagerAdapter(this)
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


        findViewById<ImageView>(R.id.courseInfo_back_ImageView).setOnClickListener {
            finish()
        }
    }
}