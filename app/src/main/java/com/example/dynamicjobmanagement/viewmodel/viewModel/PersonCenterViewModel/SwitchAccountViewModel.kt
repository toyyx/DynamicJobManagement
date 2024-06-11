package com.example.dynamicjobmanagement.viewmodel.viewModel.PersonCenterViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Student
import com.example.dynamicjobmanagement.model.model.Teacher
import com.example.dynamicjobmanagement.model.model.User
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch

class SwitchAccountViewModel: ViewModel()  {
    val switchResult = MutableLiveData<Result<String?>>()
    val deleteResult = MutableLiveData<Result<String?>>()

    fun onSwitchAccountClicked(user: User){
        if(user.account==UserRepository.getUser().account){
            switchResult.postValue(Result.failure(Exception("此用户已登录")))
        }else{
            viewModelScope.launch {
                try {
                    val response = UserRepository.login(user.account, user.password)
                    println("response:${response}")
                    println("response body:${response.body()}")
                    if (response.isSuccessful&&response.body()!=null) {
                        val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                        // 读取特定键的值
                        val result = jsonObject!!.get("result")!!.asString
                        if (result == "success") {
                            val userJsonObject=jsonObject.getAsJsonObject("userInfo")
                            //设置当前登录用户信息
                            if(jsonObject.get("userType").asString=="student"){
                                UserRepository.setUser(Gson().fromJson(userJsonObject.toString(), Student::class.java))
                                CourseRepository.setCourseIdList(UserRepository.getStudentUser()!!.courseList)
                            }else{
                                UserRepository.setUser(Gson().fromJson(userJsonObject.toString(), Teacher::class.java))
                                CourseRepository.setCourseIdList(UserRepository.getTeacherUser()!!.courseList)
                            }
                            switchResult.postValue(Result.success(userJsonObject.get("name").asString))
                        }else
                            switchResult.postValue(Result.failure(Exception("此账户登录信息已过时,请退出后重新登录")))
                    } else
                        switchResult.postValue(Result.failure(Exception("服务器响应异常")))
                } catch (e: Exception) {
                    switchResult.postValue(Result.failure(e))
                }
            }
        }
    }

    fun deleteAccount(account:String){
        UserRepository.deleteUser(account)
        deleteResult.postValue(Result.success(account))
    }

}