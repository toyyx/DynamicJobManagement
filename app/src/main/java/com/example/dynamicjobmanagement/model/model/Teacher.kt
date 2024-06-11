package com.example.dynamicjobmanagement.model.model

class Teacher(
    val teacherId:Int,
    val account: String,
    val password: String,
    val name: String,
    val university: String,
    val college: String,
    val courseList: List<String>,
){
    // 无参构造函数
    constructor() : this(0, "", "", "", "",  "", listOf())
}
