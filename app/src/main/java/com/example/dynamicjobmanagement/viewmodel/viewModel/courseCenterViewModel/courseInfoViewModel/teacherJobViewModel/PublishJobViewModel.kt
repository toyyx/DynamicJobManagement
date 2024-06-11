package com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel.teacherJobViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PublishJobViewModel: ViewModel() {
    val title= MutableLiveData<String>()
    val requirement= MutableLiveData<String>()
    var startDateTime= LocalDateTime.now()
    var endDateTime= LocalDateTime.now()
    val punishJobResult= MutableLiveData<Result<String>>()
    private var jobId: Int?=null
    private var courseId: Int?=null
    private var pubnishJobId: Int=0



    fun initCourseId(courseId:Int){
        this.courseId=courseId
    }

    fun initJob(job: Job){
        jobId=job.jobId
        title.value=job.jobTitle
        requirement.value=job.requirement
        pubnishJobId=job.jobId
        startDateTime=job.startTime
        endDateTime=job.endTime
    }

    fun setStartDate(year:Int,month:Int,day:Int){
        startDateTime=startDateTime.withYear(year).withMonth(month).withDayOfMonth(day)
    }

    fun setStartTime(hour:Int,minute:Int){
        startDateTime=startDateTime.withHour(hour).withMinute(minute)
    }

    fun setEndDate(year:Int,month:Int,day:Int){
        endDateTime=endDateTime.withYear(year).withMonth(month).withDayOfMonth(day)
    }

    fun setEndTime(hour:Int,minute:Int){
        endDateTime=endDateTime.withHour(hour).withMinute(minute)
    }

    fun publishJob(){
        if(title.value.isNullOrBlank()){
            punishJobResult.postValue(Result.failure(Exception("作业标题不可为空")))
        }else if(requirement.value.isNullOrBlank()){
            punishJobResult.postValue(Result.failure(Exception("作业要求不可为空")))
        }else if(startDateTime.isAfter(endDateTime)){
            punishJobResult.postValue(Result.failure(Exception("结束时间不可早于开始时间")))
        }else{
            viewModelScope.launch {
                try {
                    val jsonObject=JsonObject()
                    if(jobId!=null){
                        jsonObject.addProperty("jobId",jobId)
                    }
                    jsonObject.addProperty("courseId",courseId!!)
                    jsonObject.addProperty("jobTitle",title.value!!)
                    jsonObject.addProperty("requirement",requirement.value!!)
                    jsonObject.addProperty("startTime",startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    jsonObject.addProperty("endTime",endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))

                    val response = CourseRepository.publishJob(jsonObject)
                    println("response:${response}")
                    println("response body:${response.body()}")
                    if (response.isSuccessful && response.body() != null) {
                        val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                        // 读取特定键的值
                        val result = jsonObject!!.get("result")!!.asString
                        if (result == "success") {
                            punishJobResult.postValue(Result.success("发布作业成功"))
                        }else
                            punishJobResult.postValue(Result.failure(Exception("发布作业失败")))
                    }else
                        punishJobResult.postValue(Result.failure(Exception("服务器响应异常")))
                }catch (e: Exception) {
                    punishJobResult.postValue(Result.failure(e))
                }
            }
        }
    }
}