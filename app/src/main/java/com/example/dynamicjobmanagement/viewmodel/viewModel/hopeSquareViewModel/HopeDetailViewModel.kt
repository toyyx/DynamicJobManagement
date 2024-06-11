package com.example.dynamicjobmanagement.viewmodel.viewModel.hopeSquareViewModel

import LocalDateTimeAdapter
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.model.model.SolveHelp
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.HelpRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HopeDetailViewModel(seek_Help: SeekHelp) : ViewModel(){
    val acquireSolveHelpResult = MutableLiveData<Result<String?>>()
    val addLikeResult = MutableLiveData<Result<String?>>()
    val addCommentResult = MutableLiveData<Result<String?>>()
    val seekScoreOperationResult = MutableLiveData<Result<String?>>()
    val solveScoreOperationResult = MutableLiveData<Result<String?>>()

    var seekHelp: SeekHelp
    val solveHelpList = MutableLiveData<List<SolveHelp>>()

    init{
        seekHelp=seek_Help
        refreshSolveHelp()
    }

    fun refreshSolveHelp(){
        viewModelScope.launch {
            try {
                val response = HelpRepository.acquireSolveHelp(seekHelp.seekId)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val solveHelpJsonArray=jsonObject.getAsJsonArray("solveHelpInfo")
                        // 创建 GsonBuilder 实例，并注册 LocalDateTimeAdapter
                        val gson = GsonBuilder()
                            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                            .create()
                        val solveHelpListType = object : TypeToken<List<SolveHelp>>() {}.type
                        solveHelpList.value=gson.fromJson(solveHelpJsonArray, solveHelpListType)
                        acquireSolveHelpResult.postValue(Result.success("回答内容获取成功"))
                    }else
                        acquireSolveHelpResult.postValue(Result.failure(Exception("回答内容获取失败")))
                }else
                    acquireSolveHelpResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireSolveHelpResult.postValue(Result.failure(e))
            }
        }
    }

    fun onAddLikeClike(seekId:Int){
        viewModelScope.launch {
            try {
                val response = HelpRepository.addLikeToSeekHelp(seekId)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        addLikeResult.postValue(Result.success("点赞成功"))
                    }else
                        addLikeResult.postValue(Result.failure(Exception("点赞失败")))
                }else
                    addLikeResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                addLikeResult.postValue(Result.failure(e))
            }
        }
    }

    fun onAddCommentClike(solveInfo: JsonObject){
        viewModelScope.launch {
            try {
                val response = HelpRepository.addCommentToSeekHelp(solveInfo)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        // 获取当前列表
                        val currentList = solveHelpList.value ?: mutableListOf()
                        // 创建新的列表，并向其中添加元素
                        val newList = currentList.toMutableList().apply {
                            // 添加新的 SolveHelp 对象
                            add(SolveHelp(0,
                                solveInfo.get("seekId").asInt,
                                solveInfo.get("replierId").asInt,
                                solveInfo.get("replierName").asString,
                                solveInfo.get("replyContent").asString,
                                LocalDateTime.parse(solveInfo.get("replyTime").asString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                                0)
                            )
                        }
                        // 将新的列表设置回 MutableLiveData 中
                        solveHelpList.value = newList
                        addCommentResult.postValue(Result.success("评论成功"))
                    }else
                        addCommentResult.postValue(Result.failure(Exception("评论失败")))
                }else
                    addCommentResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                addCommentResult.postValue(Result.failure(e))
            }
        }
    }

    fun addSolveHelpToList(solveHelp: SolveHelp) {
        // 获取当前的值
        val currentList = solveHelpList.value ?: mutableListOf()

        // 创建一个新的可变列表，并添加新的 SolveHelp 对象
        val newList = currentList.toMutableList()
        newList.add(solveHelp)

        // 将新的列表数据通过 postValue() 方法发送到主线程
        solveHelpList.postValue(newList)
    }

    fun seekScoreOperation(seekId: Int,score:Int){
        viewModelScope.launch {
            try {
                val response = CourseRepository.seekScoreOperation(seekId,score)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        seekScoreOperationResult.postValue(Result.success("（对发起者）作业拼奖惩操作成功"))
                    }else
                        seekScoreOperationResult.postValue(Result.failure(Exception("（对发起者）作业拼奖惩操作失败")))
                }else
                    seekScoreOperationResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                seekScoreOperationResult.postValue(Result.failure(e))
            }
        }
    }

    fun solveScoreOperation(solveId: Int,score:Int){
        viewModelScope.launch {
            try {
                val response = CourseRepository.solveScoreOperation(solveId,score)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        solveScoreOperationResult.postValue(Result.success("（对参与者）作业拼奖惩操作成功"))
                    }else
                        solveScoreOperationResult.postValue(Result.failure(Exception("（对参与者）作业拼奖惩操作失败")))
                }else
                    solveScoreOperationResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                solveScoreOperationResult.postValue(Result.failure(e))
            }
        }
    }
}

