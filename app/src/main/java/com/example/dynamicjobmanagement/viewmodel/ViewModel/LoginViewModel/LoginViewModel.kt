package com.example.dynamicjobmanagement.viewmodel.ViewModel.LoginViewModel

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Student
import com.example.dynamicjobmanagement.model.model.Teacher
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    val loginResult = MutableLiveData<Result<String?>>()
    val account = ObservableField<String>()
    val password = ObservableField<String>()
    val isUserAgreementChecked= ObservableField<Boolean>()
    private val _navigateToMain = MutableLiveData<Unit>()
    val navigateToMain: LiveData<Unit> = _navigateToMain

    init {
        isUserAgreementChecked.set(false) // 设置默认值
    }

    fun onLoginButtonClicked() {
        // 在此处处理登录逻辑
        val enteredAccount = account.get()
        val enteredPassword = password.get()
        val checked=isUserAgreementChecked.get()

        if(!enteredAccount.isNullOrBlank()&&!enteredPassword.isNullOrBlank()){
            if(checked!=false){
                // 进行登录验证等操作
                viewModelScope.launch {
                    try {
                        val response = UserRepository.login(enteredAccount, enteredPassword)
                        println("response:${response}")
                        if (response.isSuccessful&&response.body()!=null) {
                            val jsonObject = response.body()
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
                                loginResult.postValue(Result.success(userJsonObject.get("name").asString))
                                _navigateToMain.value=Unit//主页面跳转信号
                            }else
                                loginResult.postValue(Result.failure(Exception("登录信息错误")))
                        } else
                            loginResult.postValue(Result.failure(Exception("服务器响应异常")))
                    } catch (e: Exception) {
                        loginResult.postValue(Result.failure(e))
                    }
                }
            }else
                loginResult.postValue(Result.failure(Exception("请阅读并同意用户协议")))
        }else
            loginResult.postValue(Result.failure(Exception("登录信息不可为空")))
    }


}
