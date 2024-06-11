package com.example.dynamicjobmanagement.model.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

//①学生用户表user_student: studentId userName password selectingList
//②教师用户表user_teacher: teacherId userName password teachingList
//③课程表course courseId: courseName teacherId assignmentList studentList
//④作业表assignment: assignmentId courseId title startTime endTime requirement
//⑤作业完成表assignment_finish: assignmentId studentId answer score
//⑥拼作业表seek_help: seekId courseId assignmentId studentId content upvoke
//⑦参与拼作业表solve_help: solveId seekId studentId content score

object RetrofitClient {
    private const val BASE_URL = "http://47.103.72.193/" // 基本URL

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()) // 如果你期望接收JSON并解析为对象
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
