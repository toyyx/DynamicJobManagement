package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.ViewPager2.TeacherJob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Job

class TeacherJobDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_job_detail)

        // 接收传递的数据
        val job = intent.getSerializableExtra("job") as Job

        findViewById<TextView>(R.id.teacherJobDetail_jobRequirement_TextView).text=job.requirement

        findViewById<ImageView>(R.id.teacherJobDetail_back_ImageView).setOnClickListener {
            finish()
        }
    }
}