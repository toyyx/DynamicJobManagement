package com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.PublishAndDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.databinding.ActivityPublishHopeBinding
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.viewmodel.ViewModel.HopeSquareViewModel.PublishHopeViewModel

class PublishHopeActivity : AppCompatActivity() {
    private lateinit var viewModel: PublishHopeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_hope)

        // 设置数据绑定
        var binding = DataBindingUtil.setContentView<ActivityPublishHopeBinding>(this, R.layout.activity_publish_hope)
        // 获取 ViewModel
        viewModel = ViewModelProvider(this).get(PublishHopeViewModel::class.java)
        // 将 ViewModel 绑定到布局
        binding.viewModel = viewModel
        // 允许 LiveData 与布局保持同步
        binding.lifecycleOwner = this

        // 检查和获取Intent携带的数据
        var data = intent.getSerializableExtra("job") as? Job
        if (data != null) {
            viewModel.initSpanner(data.courseId,data.jobId)
        }

        // 观察ViewModel中的数据变化
        viewModel.initSpannerResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.acquireJobResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.selectedCoursePosition.observe(this) {
            viewModel.acquireJobList()
        }

        viewModel.publishSeekHelpResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<ImageView>(R.id.publishHope_back_ImageView).setOnClickListener {
            finish()
        }

    }
}