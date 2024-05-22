package com.example.dynamicjobmanagement.model.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://47.103.72.193:8080/jobmanagement_server-1.0-SNAPSHOT/" // 基本URL

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()) // 如果你期望接收JSON并解析为对象
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
