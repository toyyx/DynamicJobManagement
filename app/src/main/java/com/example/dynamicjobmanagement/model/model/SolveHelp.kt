package com.example.dynamicjobmanagement.model.model

import java.time.LocalDateTime

data class SolveHelp(
    val solveId: Int,
    val seekId:Int,
    val replierId: Int,
    val replierName: String,
    val replyContent: String,
    val replyTime:LocalDateTime,
    val score:Int
)
