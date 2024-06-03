package com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class CourseMemberViewModel(memberIdList:List<String>?): ViewModel() {
    val acquireMemberResult = MutableLiveData<Result<String?>>()
    private val memberNameList = MutableLiveData<List<String>>()

    private val _filteredMembers = MutableLiveData<List<String>>()
    val filteredMembers: LiveData<List<String>> get() = _filteredMembers


    init{
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireCourseMembers(memberIdList)
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val memberJsonArray=jsonObject.getAsJsonArray("memberNameList")
                        val memberListType = object : TypeToken<List<String>>() {}.type
                        memberNameList.value=Gson().fromJson(memberJsonArray, memberListType)
                        _filteredMembers.value=memberNameList.value
                        acquireMemberResult.postValue(Result.success("课程成员信息获取成功"))
                    }else
                        acquireMemberResult.postValue(Result.failure(Exception("课程成员信息获取失败")))
                }else
                    acquireMemberResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireMemberResult.postValue(Result.failure(e))
            }
        }
    }

    fun filterMembers(query: String) {
        if(memberNameList.value!=null){
            if (query.isEmpty()) {
                _filteredMembers.value = memberNameList.value
            } else {
                _filteredMembers.value = memberNameList.value!!.filter {
                    it.contains(query, ignoreCase = true)
                }
            }
        }
    }
}