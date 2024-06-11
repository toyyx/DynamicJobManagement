package com.example.dynamicjobmanagement.model.network

import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.model.model.JobAnswer
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.model.model.SolveHelp
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST("login_android")
    suspend fun login(@Field("username") account: String, @Field("password") password: String): Response<JsonObject>

    @POST("AcquireCourse_android")
    suspend fun acquireCourse(@Body courseIdList: List<String>?): Response<JsonObject>

    @POST("AcquireMemberDetail_android")
    suspend fun acquireMemberDetail(@Body memberIdList: List<String>?): Response<JsonObject>

    @POST("AcquireJobDetail_android")
    suspend fun acquireJobDetail(@Body jobIdList: List<String>?): Response<JsonObject>

    @FormUrlEncoded
    @POST("acquireJobDetail_courseId_android")
    suspend fun acquireJobDetail_courseId(@Field("courseId") courseId: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireJobAnswer_android")
    suspend fun acquireJobAnswer(@Field("studentId") studentId: Int, @Field("jobId") jobId: Int): Response<JsonObject>

    @POST("CommitJobAnswer_android")
    suspend fun CommitJobAnswer(@Body jobAnswer: JobAnswer): Response<JsonObject>
    @FormUrlEncoded
    @POST("AcquireSeekHelp_android")
    suspend fun AcquireSeekHelp(@Field("identity") identity: String): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireSolveHelp_android")
    suspend fun AcquireSolveHelp(@Field("seekHelpId") seekHelpId: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("AddLikeToSeekHelp_android")
    suspend fun AddLikeToSeekHelp(@Field("seekHelpId") seekHelpId: Int): Response<JsonObject>


    @POST("AddCommentToSeekHelp_android")
    suspend fun AddCommentToSeekHelp(@Body comment: JsonObject): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquirePersonalSeekHelp_android")
    suspend fun AcquirePersonalSeekHelp(@Field("userId") userId: Int): Response<JsonObject>

    @POST("PublishSeekHelp_android")
    suspend fun PublishSeekHelp(@Body seekHelp: JsonObject): Response<JsonObject>

    @FormUrlEncoded
    @POST("DeleteJob_android")
    suspend fun DeleteJob(@Field("jobId") jobId: Int): Response<JsonObject>

    @POST("PublishJob_android")
    suspend fun PublishJob(@Body job: JsonObject): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireUncheckedJob_android")
    suspend fun AcquireUncheckedJob(@Field("jobId") jobId: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("CommitJobScore_android")
    suspend fun CommitJobScore(@Field("jobId") jobId: Int,@Field("studentId") studentId: Int,@Field("score") score: Double): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireJobDetailForTeacher_android")
    suspend fun AcquireJobDetailForTeacher(@Field("jobId") jobId: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("SeekScoreOperation_android")
    suspend fun SeekScoreOperation(@Field("seekId") seekId: Int,@Field("score") score: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("SolveScoreOperation_android")
    suspend fun SolveScoreOperation(@Field("solveId") solveId: Int,@Field("score") score: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireHelpDetailForTeacher_android")
    suspend fun AcquireHelpDetailForTeacher(@Field("jobId") jobId: Int): Response<JsonObject>


}