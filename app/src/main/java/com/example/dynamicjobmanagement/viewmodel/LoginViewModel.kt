package com.example.dynamicjobmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    val loginResult = MutableLiveData<Result<String?>>()
    val userName = ObservableField<String>()
    val password = ObservableField<String>()

    fun onLoginButtonClicked() {
        // 在此处处理登录逻辑
        val enteredUsername = userName.get()
        val enteredPassword = password.get()

        if(enteredUsername!=null&&enteredPassword!=null){
            // 进行登录验证等操作
            viewModelScope.launch {
                try {
                    val response = repository.login(enteredUsername, enteredPassword)
                    println("response:${response}")
                    if (response.isSuccessful) {
                        loginResult.postValue(Result.success(response.body()))
                    } else {
                        loginResult.postValue(Result.failure(Exception("Login failed")))
                    }
                } catch (e: Exception) {
                    loginResult.postValue(Result.failure(e))
                }
            }
        }
    }
}
