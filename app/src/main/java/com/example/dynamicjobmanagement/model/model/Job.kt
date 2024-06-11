package com.example.dynamicjobmanagement.model.model

import java.io.Serializable
import java.time.LocalDateTime

data class Job(
    val jobId:Int,
    val courseId: Int,
    val jobTitle: String,
    val requirement: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime,
    val commitNumber: Int
): Serializable
