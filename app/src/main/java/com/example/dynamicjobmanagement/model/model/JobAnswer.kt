package com.example.dynamicjobmanagement.model.model

data class JobAnswer (
    val jobId:Int,
    val studentId:Int,
    var studentName: String,
    var answer: String,
    val score:Double
)