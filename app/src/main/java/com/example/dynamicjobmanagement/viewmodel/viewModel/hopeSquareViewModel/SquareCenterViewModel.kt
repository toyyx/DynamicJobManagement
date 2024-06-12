package com.example.dynamicjobmanagement.viewmodel.viewModel.hopeSquareViewModel

import LocalDateTimeAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.HelpRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class SquareCenterViewModel: ViewModel()  {

    val selectedCoursePosition = MutableLiveData<Int>()

    val searchContent = MutableLiveData<String>()

    val acquireSeekHelpResult = MutableLiveData<Result<String?>>()

    private val _filteredSeekHelp = MutableLiveData<List<SeekHelp>>()
    val filteredSeekHelp: LiveData<List<SeekHelp>> get() = _filteredSeekHelp

    init{
        viewModelScope.launch {
            acquireSeekHelp()
            filterSeekHelp()
        }
    }

    suspend fun acquireSeekHelp(){
        viewModelScope.launch {
            try {
                val response = HelpRepository.acquireSeekHelp()
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val seekHelpJsonArray=jsonObject.getAsJsonArray("seekHelpInfo")
                        // 创建 GsonBuilder 实例，并注册 LocalDateTimeAdapter
                        val gson = GsonBuilder()
                            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                            .create()
                        val seekHelpListType = object : TypeToken<List<SeekHelp>>() {}.type
                        HelpRepository.setSeekHelpList(gson.fromJson(seekHelpJsonArray, seekHelpListType))
                        println("接收到的时间${HelpRepository.getSeekHelpList()!!.get(0).publishTime}")
//                        _filteredSeekHelp.value=HelpRepository.getSeekHelpList()
                        acquireSeekHelpResult.postValue(Result.success("广场信息获取成功"))
                    }else
                        acquireSeekHelpResult.postValue(Result.failure(Exception("广场信息获取失败")))
                }else
                    acquireSeekHelpResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireSeekHelpResult.postValue(Result.failure(e))
            }
        }

    }

    fun filterSeekHelp() {
        val selectedCoursePositionValue = selectedCoursePosition.value ?: 0
        if (selectedCoursePositionValue==0){
            val enteredContent=searchContent.value ?:""
            if(HelpRepository.getSeekHelpList()!=null){
                if(HelpRepository.getSeekHelpList()!!.size==0){
                    _filteredSeekHelp.value=listOf()
                }else{
                    _filteredSeekHelp.value = HelpRepository.getSeekHelpList()!!.filter {
                        it.seekContent.contains(enteredContent, ignoreCase = true)
                    }
                }
            }



        }else{
            val selectedCourse = CourseRepository.getCourseDetailList()?.get(selectedCoursePositionValue-1)

            val enteredContent=searchContent.value ?:""

            if(HelpRepository.getSeekHelpList()!=null){
                if (selectedCourse != null) {
                    _filteredSeekHelp.value = HelpRepository.getSeekHelpList()!!.filter {
                        it.courseName.contains(selectedCourse.courseName, ignoreCase = true)&&
                                it.seekContent.contains(enteredContent, ignoreCase = true)
                    }
                }
            }
        }


    }



}