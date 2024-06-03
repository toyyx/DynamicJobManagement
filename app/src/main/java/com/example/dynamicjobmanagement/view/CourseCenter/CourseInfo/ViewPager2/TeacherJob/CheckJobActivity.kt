package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.ViewPager2.TeacherJob

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.databinding.ActivityCheckJobBinding
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.TeacherJobViewModel.CheckJobViewModel

class CheckJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 设置数据绑定
        var binding = DataBindingUtil.setContentView<ActivityCheckJobBinding>(this, R.layout.activity_check_job)
        // 获取 ViewModel
        val viewModel =ViewModelProvider(this).get(CheckJobViewModel::class.java)
        // 将 ViewModel 绑定到布局
        binding.viewModel = viewModel
        // 允许 LiveData 与布局保持同步
        binding.lifecycleOwner = this

        val data = intent.getSerializableExtra("job") as Job

        findViewById<TextView>(R.id.checkJob_jobRequirement_TextView).text= data.requirement



        viewModel.initCheckJobId(data.jobId)

        // 观察ViewModel中的数据变化
        viewModel.acquireUncheckedJobResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        // 观察ViewModel中的数据变化
        viewModel.saveJobScoreResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.nowPosition.observe(this, Observer { result ->
            if(result==-1){
                findViewById<EditText>(R.id.checkJob_studentAnswer_EditText).setText("")
                findViewById<EditText>(R.id.checkJob_teacherScore_EditText).setText("")
                findViewById<EditText>(R.id.checkJob_teacherScore_EditText).isEnabled=false
            }else{
                findViewById<EditText>(R.id.checkJob_studentAnswer_EditText).setText(viewModel.uncheckedJobAnswerList.value!![result].answer)
                findViewById<EditText>(R.id.checkJob_teacherScore_EditText).setText("")
                findViewById<EditText>(R.id.checkJob_teacherScore_EditText).isEnabled=true
            }
        })
    }
}