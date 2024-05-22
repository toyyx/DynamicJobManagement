package com.example.dynamicjobmanagement.viewmodel

import com.example.dynamicjobmanagement.model.network.RetrofitClient

class UserRepository {
    suspend fun login(userName: String, password: String) = RetrofitClient.apiService.login(userName, password)
}
