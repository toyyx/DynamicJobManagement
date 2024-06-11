package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.TeacherJob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.databinding.ActivityPublishJobBinding
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel.teacherJobViewModel.PublishJobViewModel
import com.example.dynamicjobmanagement.viewmodel.viewModel.hopeSquareViewModel.PublishHopeViewModel

class PublishJobActivity : AppCompatActivity() {
    private lateinit var viewModel: PublishJobViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 设置数据绑定
        var binding = DataBindingUtil.setContentView<ActivityPublishJobBinding>(this, R.layout.activity_publish_job)
        // 获取 ViewModel
        viewModel = ViewModelProvider(this).get(PublishJobViewModel::class.java)
        // 将 ViewModel 绑定到布局
        binding.viewModel = viewModel
        // 允许 LiveData 与布局保持同步
        binding.lifecycleOwner = this

        binding.punishJobStartTimeTimePicker.setIs24HourView(true)
        binding.punishJobEndTimeTimePicker.setIs24HourView(true)

        val courseId = intent.getIntExtra("courseId",-1)
        viewModel.initCourseId(courseId)

        // 检查和获取Intent携带的数据
        val data = intent.getSerializableExtra("job") as? Job
        if (data != null) {
            viewModel.initJob(data)
            findViewById<DatePicker>(R.id.punishJob_startDate_DatePicker).updateDate(data.startTime.year,data.startTime.monthValue-1,data.startTime.dayOfMonth)
            findViewById<TimePicker>(R.id.punishJob_startTime_TimePicker).hour=data.startTime.hour
            findViewById<TimePicker>(R.id.punishJob_startTime_TimePicker).minute=data.startTime.minute
            findViewById<DatePicker>(R.id.punishJob_endDate_DatePicker).updateDate(data.endTime.year,data.endTime.monthValue-1,data.endTime.dayOfMonth)
            findViewById<TimePicker>(R.id.punishJob_endTime_TimePicker).hour=data.endTime.hour
            findViewById<TimePicker>(R.id.punishJob_endTime_TimePicker).minute=data.endTime.minute
        }

        findViewById<DatePicker>(R.id.punishJob_startDate_DatePicker).setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
//            Toast.makeText(this, "开始日期初始为${viewModel.startDateTime}", Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, "选择为\"$year-${monthOfYear + 1}-$dayOfMonth\"", Toast.LENGTH_SHORT).show()
            viewModel.setStartDate(year, monthOfYear+1, dayOfMonth)
//            Toast.makeText(this, "开始日期变化为${viewModel.startDateTime}", Toast.LENGTH_SHORT).show()
        }

        findViewById<DatePicker>(R.id.punishJob_endDate_DatePicker).setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            viewModel.setEndDate(year, monthOfYear+1, dayOfMonth)
//            Toast.makeText(this, "结束日期变化为${viewModel.endDateTime}", Toast.LENGTH_SHORT).show()
            // 观察ViewModel中的数据变化
        }

        viewModel.punishJobResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })



        findViewById<ImageView>(R.id.publishJob_back_ImageView).setOnClickListener {
            finish()
        }


    }
}