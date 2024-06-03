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
    @POST("LoginServlet")
    suspend fun login(@Field("account") account: String, @Field("password") password: String): Response<JsonObject>

    @POST("AcquireCourseServlet")
    suspend fun acquireCourse(@Body courseIdList: List<String>?): Response<JsonObject>

    @POST("AcquireMemberDetailServlet")
    suspend fun acquireMemberDetail(@Body memberIdList: List<String>?): Response<JsonObject>

    @POST("AcquireJobDetailServlet")
    suspend fun acquireJobDetail(@Body jobIdList: List<String>?): Response<JsonObject>
    @FormUrlEncoded
    @POST("AcquireJobAnswerServlet")
    suspend fun acquireJobAnswer(@Field("studentId") studentId: Int, @Field("jobId") jobId: Int): Response<JsonObject>

    @POST("CommitJobAnswerServlet")
    suspend fun CommitJobAnswer(@Body jobAnswer: JobAnswer): Response<JsonObject>
    @FormUrlEncoded
    @POST("AcquireSeekHelpServlet")
    suspend fun AcquireSeekHelp(@Field("identity") identity: String): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireSolveHelpServlet")
    suspend fun AcquireSolveHelp(@Field("seekHelpId") seekHelpId: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("AddLikeToSeekHelpServlet")
    suspend fun AddLikeToSeekHelp(@Field("seekHelpId") seekHelpId: Int): Response<JsonObject>


    @POST("AddCommentToSeekHelpServlet")
    suspend fun AddCommentToSeekHelp(@Body comment: SolveHelp): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquirePersonalSeekHelpServlet")
    suspend fun AcquirePersonalSeekHelp(@Field("userId") userId: Int): Response<JsonObject>

    @POST("PublishSeekHelpServlet")
    suspend fun PublishSeekHelp(@Body seekHelp: SeekHelp): Response<JsonObject>

    @FormUrlEncoded
    @POST("DeleteJobServlet")
    suspend fun DeleteJob(@Field("jobId") jobId: Int): Response<JsonObject>

    @POST("DeleteJobServlet")
    suspend fun PublishJob(@Body job: Job): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireUncheckedJobServlet")
    suspend fun AcquireUncheckedJob(@Field("jobId") jobId: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireUncheckedJobServlet")
    suspend fun CommitJobScore(@Field("jobId") jobId: Int,@Field("studentId") studentId: Int,@Field("score") score: Double): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireJobDetailForTeacherServlet")
    suspend fun AcquireJobDetailForTeacher(@Field("jobId") jobId: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("SeekScoreOperationServlet")
    suspend fun SeekScoreOperation(@Field("seekId") seekId: Int,@Field("score") score: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("SolveScoreOperationServlet")
    suspend fun SolveScoreOperation(@Field("solveId") solveId: Int,@Field("score") score: Int): Response<JsonObject>

    @FormUrlEncoded
    @POST("AcquireHelpDetailForTeacherServlet")
    suspend fun AcquireHelpDetailForTeacher(@Field("jobId") jobId: Int): Response<JsonObject>
}