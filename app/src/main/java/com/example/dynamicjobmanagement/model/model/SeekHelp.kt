package com.example.dynamicjobmanagement.model.model

import java.io.Serializable
import java.time.LocalDateTime

data class SeekHelp(
    val seekId:Int,
    val courseId: Int,
    val courseName: String,
    val jobId: Int,
    val jobTitle: String,
    val seekerId: Int,
    val seekerName: String,
    val seekContent: String,
    var likeNumber: Int,
    var commentNumber: Int,
    val publishTime:LocalDateTime,
    val score:Int
): Serializable
