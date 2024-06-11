package com.example.dynamicjobmanagement.viewmodel.Repository

import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.model.model.SolveHelp
import com.example.dynamicjobmanagement.model.network.RetrofitClient
import com.google.gson.JsonObject

object HelpRepository {
    private var seekHelpList:List<SeekHelp>?=null

    fun setSeekHelpList(seekHelpList:List<SeekHelp>){
        this.seekHelpList=seekHelpList
    }

    fun getSeekHelpList():List<SeekHelp>?{
        return seekHelpList
    }

    suspend fun acquireSeekHelp() = RetrofitClient.apiService.AcquireSeekHelp("student")

    suspend fun acquireSolveHelp(seekId:Int) = RetrofitClient.apiService.AcquireSolveHelp(seekId)

    suspend fun addLikeToSeekHelp(seekId:Int) = RetrofitClient.apiService.AddLikeToSeekHelp(seekId)

    suspend fun addCommentToSeekHelp(comment:JsonObject) = RetrofitClient.apiService.AddCommentToSeekHelp(comment)

    suspend fun acquirePersonalSeekHelp(userId:Int) = RetrofitClient.apiService.AcquirePersonalSeekHelp(userId)

    suspend fun publishSeekHelp(seekHelp:JsonObject) = RetrofitClient.apiService.PublishSeekHelp(seekHelp)

}