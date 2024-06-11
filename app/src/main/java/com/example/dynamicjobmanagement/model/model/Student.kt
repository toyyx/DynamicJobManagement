package com.example.dynamicjobmanagement.model.model

class Student(
    val studentId:Int=-1,
    val account: String,//学号
    val password: String,
    val name: String,
    val university: String="",
    val college: String="",
    val stu_class: String="",
    val courseList: List<String>
){

     //无参构造函数
    constructor() : this(0, "", "", "", "",  "", "",mutableListOf())
}

