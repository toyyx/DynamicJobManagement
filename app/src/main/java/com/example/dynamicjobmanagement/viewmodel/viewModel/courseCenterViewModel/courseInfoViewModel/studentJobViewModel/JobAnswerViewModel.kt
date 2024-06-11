package com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel.studentJobViewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.model.model.JobAnswer
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class JobAnswerViewModel(job: Job): ViewModel()  {
    val acquireJobAnswerResult = MutableLiveData<Result<String?>>()
    val commitJobAnswerResult = MutableLiveData<Result<String?>>()
    val jobAnswer = MutableLiveData<JobAnswer?>()
    val answerContent = MutableLiveData<String?>()
    val expiredFlag = MutableLiveData<Unit>()//作业截止信号
    lateinit var nowJob:Job
    val answerScore = MutableLiveData<String?>()
    init{
        nowJob=job
        if(job.endTime.isBefore(LocalDateTime.now())){
            expiredFlag.value=Unit
        }

        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireJobAnswer(UserRepository.getStudentUser()!!.studentId,job.jobId)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        if(jsonObject.has("jobAnswerInfo")){
                            //获取课程详细信息
                            val jobAnswerJsonObject=jsonObject.getAsJsonObject("jobAnswerInfo")
                            jobAnswer.value=Gson().fromJson(jobAnswerJsonObject, JobAnswer::class.java)
                            answerContent.value=jobAnswerJsonObject.get("answer").asString
                            answerScore.value=jobAnswerJsonObject.get("score").asString
                            acquireJobAnswerResult.postValue(Result.success("作业作答情况获取成功"))
                        }else{
                            jobAnswer.value=null
                            answerContent.value=null
                            answerScore.value="暂无"
                            acquireJobAnswerResult.postValue(Result.success("作业作答情况获取成功,暂无作答记录"))
                        }

                    }else
                        acquireJobAnswerResult.postValue(Result.failure(Exception("作业作答情况获取失败")))
                }else
                    acquireJobAnswerResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireJobAnswerResult.postValue(Result.failure(e))
            }
        }
    }

    fun onCommitButtonClicked() {
        val enteredAnswerContent=answerContent.value
        if(!enteredAnswerContent.isNullOrBlank()){
            viewModelScope.launch {
                try {
                    var tempAnswer:JobAnswer
                    if (jobAnswer.value!=null){
                        tempAnswer=jobAnswer.value!!
                        tempAnswer!!.answer=enteredAnswerContent
                    }else{
                        tempAnswer=JobAnswer(nowJob.jobId,UserRepository.getStudentUser()!!.studentId,UserRepository.getStudentUser()!!.name,enteredAnswerContent,0.0)
                    }

                    val response = CourseRepository.commitJobAnswer(tempAnswer)
                    println("response:${response}")
                    println("response body:${response.body()}")
                    if (response.isSuccessful && response.body() != null) {
                        val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                        // 读取特定键的值
                        val result = jsonObject!!.get("result")!!.asString
                        if (result == "success") {
                            //获取课程详细信息
                            commitJobAnswerResult.postValue(Result.success("作业提交成功"))
                        }else
                            commitJobAnswerResult.postValue(Result.failure(Exception("作业提交失败")))
                    }else
                        commitJobAnswerResult.postValue(Result.failure(Exception("服务器响应异常")))
                }catch (e: Exception) {
                    commitJobAnswerResult.postValue(Result.failure(e))
                }
            }
        }else
            commitJobAnswerResult.postValue(Result.failure(Exception("作答不可为空")))
    }

    fun getJobAnswer():JobAnswer?{
        return jobAnswer.value
    }

}