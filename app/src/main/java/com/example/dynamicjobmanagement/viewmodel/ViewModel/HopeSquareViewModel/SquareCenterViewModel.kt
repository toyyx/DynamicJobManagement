package com.example.dynamicjobmanagement.viewmodel.ViewModel.HopeSquareViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.HelpRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class SquareCenterViewModel: ViewModel()  {
    private val _spinnerItems = MutableLiveData<List<String>>()
    val spinnerItems: LiveData<List<String>> get() = _spinnerItems
        val selectedCoursePosition = MutableLiveData<Int>()

    val searchContent = MutableLiveData<String>()

    val acquireSeekHelpResult = MutableLiveData<Result<String?>>()

    private val _filteredSeekHelp = MutableLiveData<List<SeekHelp>>()
    val filteredSeekHelp: LiveData<List<SeekHelp>> get() = _filteredSeekHelp

    init{
        _spinnerItems.value=CourseRepository.getCourseDetailList()?.map { it.courseName }
        viewModelScope.launch {
            try {
                val response = HelpRepository.acquireSeekHelp()
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val seekHelpJsonArray=jsonObject.getAsJsonArray("seekHelpInfo")
                        val seekHelpListType = object : TypeToken<List<String>>() {}.type
                        HelpRepository.setSeekHelpList(Gson().fromJson(seekHelpJsonArray, seekHelpListType))
                        _filteredSeekHelp.value=HelpRepository.getSeekHelpList()
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
        val selectedCourse = _spinnerItems.value?.get(selectedCoursePositionValue) ?: ""

        val enteredContent=searchContent.value ?:""

        if(HelpRepository.getSeekHelpList()!=null){
            _filteredSeekHelp.value = HelpRepository.getSeekHelpList()!!.filter {
                it.courseName.contains(selectedCourse, ignoreCase = true)&&
                        it.seekContent.contains(enteredContent, ignoreCase = true)
            }
        }
    }

}