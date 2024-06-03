package com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.TeacherJobViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PublishJobViewModel: ViewModel() {
    val title= MutableLiveData<String>()
    val requirement= MutableLiveData<String>()
    val startDateTime= MutableLiveData<LocalDateTime>()
    val endDateTime= MutableLiveData<LocalDateTime>()
    val punishJobResult= MutableLiveData<Result<String>>()

    private var courseId: Int?=null
    private var pubnishJobId: Int=0

    init {
        startDateTime.value=LocalDateTime.now()
        endDateTime.value=LocalDateTime.now()
    }

    fun initCourseId(courseId:Int){
        this.courseId=courseId
    }

    fun initJob(job: Job){
        title.value=job.jobTitle
        requirement.value=job.requirement
        pubnishJobId=job.jobId
    }

    fun setStartDate(year:Int,month:Int,day:Int){
        startDateTime.value!!.withYear(year).withMonth(month).withDayOfMonth(day)
    }

    fun setStartTime(hour:Int,minute:Int){
        startDateTime.value!!.withHour(hour).withMinute(minute)
    }

    fun setEndDate(year:Int,month:Int,day:Int){
        endDateTime.value!!.withYear(year).withMonth(month).withDayOfMonth(day)
    }

    fun setEndTime(hour:Int,minute:Int){
        endDateTime.value!!.withHour(hour).withMinute(minute)
    }

    fun publishJob(){
        if(title.value.isNullOrBlank()){
            punishJobResult.postValue(Result.failure(Exception("作业标题不可为空")))
        }else if(requirement.value.isNullOrBlank()){
            punishJobResult.postValue(Result.failure(Exception("作业要求不可为空")))
        }else if(startDateTime.value!!.isAfter(endDateTime.value)){
            punishJobResult.postValue(Result.failure(Exception("开始时间不可早于结束时间")))
        }else{
            viewModelScope.launch {
                try {
                    val response = CourseRepository.publishJob(Job(pubnishJobId,courseId!!,title.value!!,requirement.value!!,startDateTime.value!!,endDateTime.value!!,0))
                    println("response:${response}")
                    if (response.isSuccessful && response.body() != null) {
                        val jsonObject = response.body()
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