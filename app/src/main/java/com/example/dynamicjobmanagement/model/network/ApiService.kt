package com.example.dynamicjobmanagement.model.network

import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("LoginServlet")
    suspend fun login(@Field("userName") username: String, @Field("password") password: String): Response<String>
}