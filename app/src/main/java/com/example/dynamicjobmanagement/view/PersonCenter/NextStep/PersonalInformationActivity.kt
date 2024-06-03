package com.example.dynamicjobmanagement.view.PersonCenter.NextStep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository

class PersonalInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_information)

        if(UserRepository.getUserType()==UserType.STUDENT){
            val student=UserRepository.getStudentUser()
            findViewById<LinearLayout>(R.id.personInfo_teacher_exclusive_LinearLayout).visibility= View.GONE
            findViewById<TextView>(R.id.personInfo_name_TextView).text=student!!.name
            findViewById<TextView>(R.id.personInfo_university_TextView).text=student.university
            findViewById<TextView>(R.id.personInfo_college_TextView).text=student.college
            findViewById<TextView>(R.id.personInfo_class_TextView).text=student.stu_class
            findViewById<TextView>(R.id.personInfo_StudentAccount_TextView).text=student.account
        }else{
            val teacher=UserRepository.getTeacherUser()
            findViewById<LinearLayout>(R.id.personInfo_student_exclusive_LinearLayout).visibility= View.GONE
            findViewById<TextView>(R.id.personInfo_name_TextView).text=teacher!!.name
            findViewById<TextView>(R.id.personInfo_university_TextView).text= teacher.university
            findViewById<TextView>(R.id.personInfo_college_TextView).text=teacher.college
            findViewById<TextView>(R.id.personInfo_TeacherAccount_TextView).text=teacher.account
        }

        findViewById<ImageView>(R.id.personInfo_back_ImageView).setOnClickListener {
            finish()
        }
    }
}