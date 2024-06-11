package com.example.dynamicjobmanagement.viewmodel.viewModel.hopeSquareViewModel

import LocalDateTimeAdapter
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
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PublishHopeViewModel:ViewModel() {

    val selectedCoursePosition = MutableLiveData<Int>()

    val jobSpinnerItems = MutableLiveData<List<String>>()
    val selectedJobPosition : MutableLiveData<Int> = MutableLiveData(0)

    val seekContent=MutableLiveData<String>()

    val jobList = MutableLiveData<List<Job>>()

    val acquireJobResult = MutableLiveData<Result<String?>>()
    val publishSeekHelpResult = MutableLiveData<Result<String?>>()
    val initSpannerResult = MutableLiveData<Result<String?>>()



    fun initSpanner(courseId:Int,jobId:Int){
        val coursePosition = CourseRepository.getCourseDetailList()!!.indexOfFirst { it.courseId==courseId }
        if (coursePosition != -1) {
            val jobPosition  =CourseRepository.getCourseDetailList()!![coursePosition].jobList.indexOfFirst{ it==jobId.toString() }
            if(jobPosition != -1){
                viewModelScope.launch {
                    try {
                        val selectedCourse = CourseRepository.getCourseDetailList()!!.get(coursePosition)
                        val response = CourseRepository.acquireCourseJobs(selectedCourse!!.jobList)
                        println("initSpanner response:${response}")
                        println("initSpanner response body:${response.body()}")
                        if (response.isSuccessful && response.body() != null) {
                            val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                            // 读取特定键的值
                            val result = jsonObject!!.get("result")!!.asString
                            if (result == "success") {
                                //获取课程详细信息
                                val jobJsonArray=jsonObject.getAsJsonArray("jobInfo")
                                // 创建 GsonBuilder 实例，并注册 LocalDateTimeAdapter
                                val gson = GsonBuilder()
                                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                                    .create()
                                val jobListType = object : TypeToken<List<Job>>() {}.type
                                jobList.value=gson.fromJson(jobJsonArray, jobListType)
                                selectedCoursePosition.value=coursePosition
                                jobSpinnerItems.value=jobList.value!!.map { it.jobTitle }
                                selectedJobPosition.value=jobPosition
                                acquireJobResult.postValue(Result.success("快捷作业信息获取成功"))
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
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val jobJsonArray=jsonObject.getAsJsonArray("jobInfo")
                        // 创建 GsonBuilder 实例，并注册 LocalDateTimeAdapter
                        val gson = GsonBuilder()
                            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                            .create()
                        val jobListType = object : TypeToken<List<Job>>() {}.type
                        jobList.value=gson.fromJson(jobJsonArray, jobListType)
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
        println("当前作业列表${jobList.value}")
        if(CourseRepository.getCourseDetailList()!!.size<=0) {
            publishSeekHelpResult.postValue(Result.failure(Exception("暂无可选课程")))
            return
        }
        val selectedCoursePositionValue = selectedCoursePosition.value ?: 0
        val selectedCourse = CourseRepository.getCourseDetailList()?.get(selectedCoursePositionValue)
        if (jobList.value!!.isEmpty()){
            publishSeekHelpResult.postValue(Result.failure(Exception("当前课程暂无可选作业")))
            return
        }
        val selectedJobPositionValue = selectedJobPosition.value ?: 0
        val selectedJob = jobList.value!!.get(selectedJobPositionValue)

        val enteredContent=seekContent.value
        if(selectedCourse==null) {
            publishSeekHelpResult.postValue(Result.failure(Exception("课程信息获取异常")))
            return
        }
        else if(enteredContent==null){
            publishSeekHelpResult.postValue(Result.failure(Exception("问题描述信息不可为空")))
            return
        }
        else if(enteredContent.isBlank()){
            publishSeekHelpResult.postValue(Result.failure(Exception("问题描述不可为空")))
            return
        }
        else{
            viewModelScope.launch {
                try {
                    var userId: Int
                    var seekerName:String
                    if(UserRepository.getUserType()== UserType.STUDENT){
                        userId=UserRepository.getStudentUser()!!.studentId
                        seekerName=UserRepository.getStudentUser()!!.name
                    }else{
                        userId=UserRepository.getTeacherUser()!!.teacherId
                        seekerName=UserRepository.getTeacherUser()!!.name
                    }
                    val jsonObject=JsonObject()
                    jsonObject.addProperty("courseId",selectedCourse.courseId)
                    jsonObject.addProperty("courseName",selectedCourse.courseName)
                    jsonObject.addProperty("jobId",selectedJob.jobId)
                    jsonObject.addProperty("jobTitle",selectedJob.jobTitle)
                    jsonObject.addProperty("seekerId",userId)
                    jsonObject.addProperty("seekerName",seekerName)
                    jsonObject.addProperty("seekContent",enteredContent)
                    jsonObject.addProperty("publishTime",LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    val response = HelpRepository.publishSeekHelp(jsonObject)
                    println("response:${response}")
                    println("response body:${response.body()}")
                    if (response.isSuccessful && response.body() != null) {
                        val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
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