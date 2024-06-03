package com.example.dynamicjobmanagement.model.model

import java.io.Serializable

data class Course (
    val courseId:Int,
    val courseName: String,
    val teacherId: Int,
    val teacherName: String,
    val courseType: String,
    val credit: Double,
    val courseTime: String,
    val courseAddress: String,
    val jobList:List<String>,
    val studentList:List<String>
): Serializable