package com.example.dynamicjobmanagement.viewmodel.viewModel.loginViewModel

import StudentInstanceCreator
import TeacherInstanceCreator
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
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
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
                        println("response body:${response.body()}")
                        if (response.isSuccessful&&response.body()!=null) {
                            val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                            // 读取特定键的值
                            val result = jsonObject.get("result").asString
                            if (result == "success") {
                                val userJsonObject=jsonObject.getAsJsonObject("userInfo")
//                                val gson = GsonBuilder()
//                                    .registerTypeAdapter(Student::class.java, StudentInstanceCreator())
//                                    .registerTypeAdapter(Teacher::class.java, TeacherInstanceCreator())
//                                    .create()
                                //设置当前登录用户信息
                                if(jsonObject.get("userType").asString=="student"){
                                    UserRepository.setUser(Gson().fromJson(userJsonObject, Student::class.java))
                                    //UserRepository.setUser(fromJsonObjectToStudent(userJsonObject))
                                    CourseRepository.setCourseIdList(UserRepository.getStudentUser()!!.courseList)
                                }else{
                                    UserRepository.setUser(Gson().fromJson(userJsonObject, Teacher::class.java))
                                    //UserRepository.setUser(fromJsonObjectToTeacher(userJsonObject))
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

    fun fromJsonObjectToStudent(jsonObject: JsonObject):Student{
        return Student(jsonObject.get("studentId").asInt,
            jsonObject.get("account").asString,
            jsonObject.get("password").asString,
            jsonObject.get("name").asString,
            jsonObject.get("university").asString,
            jsonObject.get("college").asString,
            jsonObject.get("stu_class").asString,
            jsonObject.getAsJsonArray("courseList").map { it.asString })
    }

    fun fromJsonObjectToTeacher(jsonObject: JsonObject):Teacher{
        return Teacher(jsonObject.get("teacherId").asInt,
            jsonObject.get("account").asString,
            jsonObject.get("password").asString,
            jsonObject.get("name").asString,
            jsonObject.get("university").asString,
            jsonObject.get("college").asString,
            jsonObject.getAsJsonArray("courseList").map { it.asString })
    }

}
