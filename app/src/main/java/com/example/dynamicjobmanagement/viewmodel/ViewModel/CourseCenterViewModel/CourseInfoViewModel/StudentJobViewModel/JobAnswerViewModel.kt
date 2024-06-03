package com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.StudentJobViewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.model.model.JobAnswer
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class JobAnswerViewModel(job: Job): ViewModel()  {
    val acquireJobAnswerResult = MutableLiveData<Result<String?>>()
    val commitJobAnswerResult = MutableLiveData<Result<String?>>()
    val jobAnswer = MutableLiveData<JobAnswer>()
    val answerContent = ObservableField<String>()
    val expiredFlag = MutableLiveData<Unit>()//作业截止信号


    init{
        if(job.endTime.isBefore(LocalDateTime.now())){
            expiredFlag.value=Unit
        }

        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireJobAnswer(UserRepository.getStudentUser()!!.studentId,job.jobId)
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val jobAnswerJsonObject=jsonObject.getAsJsonObject("jobAnswerInfo")
                        jobAnswer.value=Gson().fromJson(jobAnswerJsonObject, JobAnswer::class.java)
                        acquireJobAnswerResult.postValue(Result.success("作业作答情况获取成功"))
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
        val enteredAnswerContent=answerContent.get()
        if(!enteredAnswerContent.isNullOrBlank()){
            viewModelScope.launch {
                try {
                    val tempAnswer=jobAnswer.value
                    tempAnswer!!.answer=enteredAnswerContent
                    val response = CourseRepository.commitJobAnswer(tempAnswer)
                    println("response:${response}")
                    if (response.isSuccessful && response.body() != null) {
                        val jsonObject = response.body()
                        // 读取特定键的值
                        val result = jsonObject!!.get("result")!!.asString
                        if (result == "success") {
                            //获取课程详细信息
                            jobAnswer.value!!.answer=enteredAnswerContent
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

    fun getJobAnswer():JobAnswer{
        return jobAnswer.value!!
    }

}