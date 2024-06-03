package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.ViewPager2.StudentJob

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.databinding.ActivityJobAnswerBinding
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.PublishAndDetail.PublishHopeActivity
import com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.StudentJobViewModel.JobAnswerViewModel
import com.example.dynamicjobmanagement.viewmodel.Factory.CourseCenterFactory.JobAnswerViewModelFactory

class JobAnswerActivity : AppCompatActivity() {

    private lateinit var viewModel: JobAnswerViewModel
    private lateinit var jobTitle_TV: TextView
    private lateinit var jobRequirement_TV: TextView
    private lateinit var answerContent_ET: EditText
    private lateinit var score_TV: TextView
    private lateinit var commit_B: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 接收传递的数据
        val job = intent.getSerializableExtra("job") as Job

        // 设置数据绑定
        var binding = DataBindingUtil.setContentView<ActivityJobAnswerBinding>(this, R.layout.activity_job_answer)
        // 获取 ViewModel
        viewModel = ViewModelProvider(this, JobAnswerViewModelFactory(job)).get(JobAnswerViewModel::class.java)
        // 将 ViewModel 绑定到布局
        binding.viewModel = viewModel
        // 允许 LiveData 与布局保持同步
        binding.lifecycleOwner = this


        jobTitle_TV=findViewById(R.id.jobAnswer_title_TextView)
        jobRequirement_TV=findViewById(R.id.jobAnswer_requirement_TextView)
        answerContent_ET=findViewById(R.id.jobAnswer_answerContent_EditText)
        score_TV=findViewById(R.id.jobAnswer_score_TextView)
        commit_B=findViewById(R.id.jobAnswer_commit_Button)

        jobTitle_TV.text=job.jobTitle
        jobRequirement_TV.text=job.requirement

        // 观察ViewModel中的数据变化
        viewModel.expiredFlag.observe(this, Observer { result ->
            answerContent_ET.isEnabled=false
            commit_B.visibility= View.GONE
        })

        // 观察ViewModel中的数据变化
        viewModel.acquireJobAnswerResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                answerContent_ET.setText(viewModel.getJobAnswer().answer)
                score_TV.text="作答评分:${viewModel.getJobAnswer().score}"
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        // 观察ViewModel中的数据变化
        viewModel.commitJobAnswerResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<ImageView>(R.id.jobAnswer_back_ImageView).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.jobAnswer_launch_ImageView).setOnClickListener {
            showLaunchConfirmDialog(this,job)
        }
    }

    fun showLaunchConfirmDialog(context:Context,job: Job){
        AlertDialog.Builder(this).apply {
            setTitle("发起作业拼")
            setMessage("确定将当前作业发布至希望广场？")
            setPositiveButton("是") { dialog, _ ->
                val intent = Intent(context, PublishHopeActivity::class.java)
                intent.putExtra("job", job)
                startActivity(intent)
                dialog.dismiss()
            }
            setNegativeButton("否") { dialog, _ ->
                // 用户点击了 "否"，关闭对话框
                dialog.dismiss()
            }
            create()
            show()
        }
    }
}