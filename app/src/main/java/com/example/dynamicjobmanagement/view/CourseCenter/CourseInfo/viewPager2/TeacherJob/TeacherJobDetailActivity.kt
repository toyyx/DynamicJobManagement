package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.TeacherJob

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Job
import java.time.format.DateTimeFormatter

class TeacherJobDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_job_detail)

        // 接收传递的数据
        val job = intent.getSerializableExtra("job") as Job

        findViewById<TextView>(R.id.teacherJobDetail_jobRequirement_TextView).text="        ${job.requirement}"
        findViewById<TextView>(R.id.teacherJobDetail_jobTitle_TextView).text=job.jobTitle
        findViewById<TextView>(R.id.teacherJobDetail_jobTime_TextView).text="${
            job.startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}\n至\n" +
                "${job.endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))}"

        findViewById<ImageView>(R.id.teacherJobDetail_back_ImageView).setOnClickListener {
            finish()
        }
    }
}