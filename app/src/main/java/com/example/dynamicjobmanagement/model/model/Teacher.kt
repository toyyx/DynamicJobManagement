package com.example.dynamicjobmanagement.model.model

data class Teacher(
    val teacherId:Int,
    override val account: String,
    override val password: String,
    override val name: String,
    val university: String,
    val college: String,
    val courseList: List<String>,
): User
