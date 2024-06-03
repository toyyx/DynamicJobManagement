package com.example.dynamicjobmanagement.model.model

data class Student(
    val studentId:Int,
    override val account: String,//学号
    override val password: String,
    override val name: String,
    val university: String,
    val college: String,
    val stu_class: String,
    val courseList: List<String>
): User
