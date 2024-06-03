package com.example.dynamicjobmanagement.viewmodel.ViewModel.HopeSquareViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.viewmodel.Repository.HelpRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class MySquareViewModel(): ViewModel(){
    val acquireMySeekHelpResult = MutableLiveData<Result<String?>>()

    val MyseekHelpList = MutableLiveData<List<SeekHelp>>()


    init{
        viewModelScope.launch {
            try {
                var userId: Int
                if(UserRepository.getUserType()==UserType.STUDENT){
                    userId=UserRepository.getStudentUser()!!.studentId
                }else{
                    userId=UserRepository.getTeacherUser()!!.teacherId
                }
                val response = HelpRepository.acquirePersonalSeekHelp(userId)
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val seekHelpJsonArray=jsonObject.getAsJsonArray("mySeekHelpInfo")
                        val seekHelpListType = object : TypeToken<List<SeekHelp>>() {}.type
                        MyseekHelpList.value= Gson().fromJson(seekHelpJsonArray, seekHelpListType)
                        acquireMySeekHelpResult.postValue(Result.success("个人作业拼信息获取成功"))
                    }else
                        acquireMySeekHelpResult.postValue(Result.failure(Exception("个人作业拼信息获取失败")))
                }else
                    acquireMySeekHelpResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireMySeekHelpResult.postValue(Result.failure(e))
            }
        }
    }



}