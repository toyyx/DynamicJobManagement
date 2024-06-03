package com.example.dynamicjobmanagement.viewmodel.ViewModel.HopeSquareViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.HelpRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class PublishHopeViewModel:ViewModel() {
    val courseSpinnerItems = MutableLiveData<List<String>>()
    val selectedCoursePosition = MutableLiveData<Int>()

    val jobSpinnerItems = MutableLiveData<List<String>>()
    val selectedJobPosition = MutableLiveData<Int>()

    val seekContent=MutableLiveData<String>()

    val jobList = MutableLiveData<List<Job>>()

    val acquireJobResult = MutableLiveData<Result<String?>>()
    val publishSeekHelpResult = MutableLiveData<Result<String?>>()
    val initSpannerResult = MutableLiveData<Result<String?>>()

    init {
        courseSpinnerItems.value=CourseRepository.getCourseDetailList()!!.map { it.courseName }
        selectedCoursePosition.value=0
        acquireJobList()
        selectedJobPosition.value=0
    }

    fun initSpanner(courseId:Int,jobId:Int){
        val coursePosition = CourseRepository.getCourseDetailList()!!.indexOfFirst { it.courseId==courseId }
        if (coursePosition != -1) {
            val jobPosition  =CourseRepository.getCourseDetailList()!![coursePosition].jobList.indexOfFirst{ it==jobId.toString() }
            if(jobPosition != -1){
                viewModelScope.launch {
                    try {
                        val selectedCourse = CourseRepository.getCourseDetailList()?.get(coursePosition)
                        val response = CourseRepository.acquireCourseJobs(selectedCourse!!.jobList)
                        println("response:${response}")
                        if (response.isSuccessful && response.body() != null) {
                            val jsonObject = response.body()
                            // 读取特定键的值
                            val result = jsonObject!!.get("result")!!.asString
                            if (result == "success") {
                                //获取课程详细信息
                                val jobJsonArray=jsonObject.getAsJsonArray("jobInfo")
                                val jobListType = object : TypeToken<List<Job>>() {}.type
                                jobList.value=Gson().fromJson(jobJsonArray, jobListType)
                                jobSpinnerItems.value=jobList.value!!.map { it.jobTitle }
                                selectedJobPosition.value=jobPosition
                                acquireJobResult.postValue(Result.success("作业信息获取成功"))
                            }else
                                acquireJobResult.postValue(Result.failure(Exception("作业信息获取失败")))
                        }else
                            acquireJobResult.postValue(Result.failure(Exception("服务器响应异常")))
                    }catch (e: Exception) {
                        acquireJobResult.postValue(Result.failure(e))
                    }
                }
                initSpannerResult.postValue(Result.success("传递数据成功"))
            }else
                initSpannerResult.postValue(Result.failure(Exception("传递数据失败")))
        }else
            initSpannerResult.postValue(Result.failure(Exception("传递数据失败")))
    }

    fun acquireJobList(){
        viewModelScope.launch {
            try {
                val selectedCoursePositionValue = selectedCoursePosition.value!!
                val selectedCourse = CourseRepository.getCourseDetailList()?.get(selectedCoursePositionValue)
                val response = CourseRepository.acquireCourseJobs(selectedCourse!!.jobList)
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val jobJsonArray=jsonObject.getAsJsonArray("jobInfo")
                        val jobListType = object : TypeToken<List<Job>>() {}.type
                        jobList.value=Gson().fromJson(jobJsonArray, jobListType)
                        jobSpinnerItems.value=jobList.value!!.map { it.jobTitle }
                        selectedJobPosition.value=0
                        acquireJobResult.postValue(Result.success("作业信息获取成功"))
                    }else
                        acquireJobResult.postValue(Result.failure(Exception("作业信息获取失败")))
                }else
                    acquireJobResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireJobResult.postValue(Result.failure(e))
            }
        }
    }


    fun onPublishSeekHelpClicked(){
        val selectedCoursePositionValue = selectedCoursePosition.value ?: 0
        val selectedCourse = CourseRepository.getCourseDetailList()?.get(selectedCoursePositionValue)

        val selectedJobPositionValue = selectedJobPosition.value ?: 0
        val selectedJob = jobList.value?.get(selectedJobPositionValue)

        val enteredContent=seekContent.value
        if(selectedCourse==null)
            publishSeekHelpResult.postValue(Result.failure(Exception("课程信息获取失败")))
        else if(selectedJob==null)
            publishSeekHelpResult.postValue(Result.failure(Exception("作业信息获取失败")))
        else if(enteredContent==null)
            publishSeekHelpResult.postValue(Result.failure(Exception("问题描述信息获取失败")))
        else if(enteredContent.isBlank())
            publishSeekHelpResult.postValue(Result.failure(Exception("问题描述不可为空")))
        else{
            viewModelScope.launch {
                try {
                    var userId: Int
                    if(UserRepository.getUserType()== UserType.STUDENT){
                        userId=UserRepository.getStudentUser()!!.studentId
                    }else{
                        userId=UserRepository.getTeacherUser()!!.teacherId
                    }
                    val response = HelpRepository.publishSeekHelp(SeekHelp(0,
                        selectedCourse.courseId,
                        selectedCourse.courseName,
                        selectedJob.jobId,
                        selectedJob.jobTitle,
                        userId,
                        UserRepository.getStudentUser()!!.name,
                        enteredContent,
                        0,0, LocalDateTime.now(),0
                    ))
                    println("response:${response}")
                    if (response.isSuccessful && response.body() != null) {
                        val jsonObject = response.body()
                        // 读取特定键的值
                        val result = jsonObject!!.get("result")!!.asString
                        if (result == "success") {
                            publishSeekHelpResult.postValue(Result.success("发布成功"))
                        }else
                            publishSeekHelpResult.postValue(Result.failure(Exception("发布失败")))
                    }else
                        publishSeekHelpResult.postValue(Result.failure(Exception("服务器响应异常")))
                }catch (e: Exception) {
                    publishSeekHelpResult.postValue(Result.failure(e))
                }
            }
        }
    }


}