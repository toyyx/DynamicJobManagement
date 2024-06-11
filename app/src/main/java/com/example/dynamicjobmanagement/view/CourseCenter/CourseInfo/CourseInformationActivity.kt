package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo


import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Course
import com.example.dynamicjobmanagement.model.model.Refreshable
import com.example.dynamicjobmanagement.view.Adapter.CourseCenter.CourseInfoViewPagerAdapter
import com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel.CourseInfoViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CourseInformationActivity : AppCompatActivity() {
    private val viewModel: CourseInfoViewModel by viewModels()
    private lateinit var adapter: CourseInfoViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_information)

        // 接收传递的数据
        val course = intent.getSerializableExtra("course") as? Course
        viewModel.course.value=course

        val viewPager = findViewById<ViewPager2>(R.id.courseInfo_ViewPager2)
        val tabLayout = findViewById<TabLayout>(R.id.courseInfo_TabLayout)

        adapter = CourseInfoViewPagerAdapter(this)
        viewPager.adapter = adapter

        // 将TabLayout与ViewPager2关联
        TabLayoutMediator(tabLayout, viewPager) {
                tab, position ->
            // 根据position设置Tab的标题
            tab.text = when (position) {
                0 -> "基本信息"
                1 -> "作业"
                2 -> "成员"
                else -> throw IllegalArgumentException("Invalid position")
            }
        }.attach()

//        // 设置页面变化监听器
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                // 仅在特定页面被选中时刷新数据
//                if (position == 1) { // 假设第一个页面需要刷新数据
//                    val fragment = adapter.getFragment(position)
//                    if (fragment is Refreshable) {
//                        fragment.refreshData()
//                    }
//                }
//            }
//        })


        findViewById<ImageView>(R.id.courseInfo_back_ImageView).setOnClickListener {
            finish()
        }
    }
}