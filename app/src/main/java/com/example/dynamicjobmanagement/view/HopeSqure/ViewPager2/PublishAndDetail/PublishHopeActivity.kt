package com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.PublishAndDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.databinding.ActivityPublishHopeBinding
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.viewModel.hopeSquareViewModel.PublishHopeViewModel
import kotlinx.coroutines.launch

class PublishHopeActivity : AppCompatActivity() {
    private lateinit var viewModel: PublishHopeViewModel
    private lateinit var courseSpinnerAdapter: ArrayAdapter<String>
    private lateinit var jobSpinnerAdapter: ArrayAdapter<String>
    private var preventFresh =2
    private val lock = Any()
    private var check=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置数据绑定
        var binding = DataBindingUtil.setContentView<ActivityPublishHopeBinding>(this, R.layout.activity_publish_hope)
        // 获取 ViewModel
        viewModel = ViewModelProvider(this).get(PublishHopeViewModel::class.java)
        // 将 ViewModel 绑定到布局
        binding.viewModel = viewModel
        // 允许 LiveData 与布局保持同步
        binding.lifecycleOwner = this

        val courseSpinner=binding.publishJobCourseSpinner
        // 下拉选项内容
        val courseOptions = CourseRepository.getCourseDetailList()?.map { it.courseName }
        // 创建适配器并设置下拉内容
        courseSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, courseOptions!!)
        courseSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // 设置适配器
        courseSpinner.adapter = courseSpinnerAdapter

        val jobSpinner=binding.publishJobJobSpinner
        // 创建适配器并设置下拉内容
        jobSpinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        jobSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // 设置适配器
        jobSpinner.adapter = jobSpinnerAdapter

        // 检查和获取Intent携带的数据
        var data = intent.getSerializableExtra("job") as? Job


        if (data != null) {
            if(CourseRepository.getCourseIdList()!!.get(0)!=data.courseId.toString()){
                preventFresh+=1
            }
            viewModel.initSpanner(data.courseId,data.jobId)
        }else{
            viewModel.selectedCoursePosition.value=0
            viewModel.acquireJobList()

        }


        // 观察ViewModel中的数据变化
        viewModel.initSpannerResult.observe(this, Observer { result ->
            result.onSuccess {info ->
//                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.acquireJobResult.observe(this, Observer { result ->
            result.onSuccess {info ->
//                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this,"xxxx", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.selectedCoursePosition.observe(this) {
//            Toast.makeText(this, "课程位置已变化", Toast.LENGTH_SHORT).show()
            if(check){
                synchronized(lock) {
                    println("课程位置已变化${preventFresh}")
                    if(preventFresh>0){
                        preventFresh -= 1
                    }else{
                        viewModel.acquireJobList()
                        check=false
                    }
                }
            }else{
                viewModel.acquireJobList()
            }

        }

        viewModel.publishSeekHelpResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        // 观察ViewModel中的数据变化
        viewModel.jobSpinnerItems.observe(this, Observer { newData ->
//            Toast.makeText(this, "已更新", Toast.LENGTH_SHORT).show()
            jobSpinnerAdapter.clear()
            // 添加新数据
            jobSpinnerAdapter.addAll(newData)
            // 通知适配器数据已更改
            jobSpinnerAdapter.notifyDataSetChanged()
        })

        findViewById<ImageView>(R.id.publishHope_back_ImageView).setOnClickListener {
            finish()
        }

    }
}