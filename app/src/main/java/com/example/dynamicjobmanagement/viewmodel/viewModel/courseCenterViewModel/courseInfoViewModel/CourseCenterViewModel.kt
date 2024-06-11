package com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Course
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class CourseCenterViewModel: ViewModel() {
    val acquireCourseResult = MutableLiveData<Result<String?>>()

    private val _filteredCourses = MutableLiveData<List<Course>>()
    val filteredCourses: LiveData<List<Course>> get() = _filteredCourses


    init{
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireCourse()
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val courseJsonArray=jsonObject.getAsJsonArray("courseInfo")
                        val courseListType = object : TypeToken<List<Course>>() {}.type
                        CourseRepository.setCourseList(Gson().fromJson(courseJsonArray, courseListType))
                        acquireCourseResult.postValue(Result.success("课程信息获取成功"))
                    }else
                        acquireCourseResult.postValue(Result.failure(Exception("课程信息获取失败")))
                }else
                    acquireCourseResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireCourseResult.postValue(Result.failure(e))
            }
        }
    }

    fun filterCourses(query: String) {
        if(CourseRepository.getCourseDetailList()!=null){
            if (query.isEmpty()) {
                _filteredCourses.value = CourseRepository.getCourseDetailList()
            } else {
                _filteredCourses.value = CourseRepository.getCourseDetailList()!!.filter {
                    it.courseName.contains(query, ignoreCase = true)
                }
            }
        }
    }
}