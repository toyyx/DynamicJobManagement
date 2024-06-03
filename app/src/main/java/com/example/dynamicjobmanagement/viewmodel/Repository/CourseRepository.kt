package com.example.dynamicjobmanagement.viewmodel.Repository

import com.example.dynamicjobmanagement.model.model.Course
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.model.model.JobAnswer
import com.example.dynamicjobmanagement.model.network.RetrofitClient

object CourseRepository {
    private var courseIdList:List<String>?=null

    private var courseDetailList:List<Course>?=null

    fun setCourseIdList(courseIdList:List<String>){
        this.courseIdList=courseIdList
    }

    fun setCourseList(courseList:List<Course>){
        this.courseDetailList=courseList
    }

    fun getCourseIdList():List<String>?{
        return courseIdList
    }

    fun getCourseDetailList():List<Course>?{
        return courseDetailList
    }

    suspend fun acquireCourse() = RetrofitClient.apiService.acquireCourse(courseIdList)

    suspend fun acquireCourseMembers(memberIdList:List<String>?) = RetrofitClient.apiService.acquireMemberDetail(memberIdList)

    suspend fun acquireCourseJobs(jobIdList:List<String>?) = RetrofitClient.apiService.acquireJobDetail(jobIdList)

    suspend fun acquireJobAnswer(studentId:Int,jobId:Int) = RetrofitClient.apiService.acquireJobAnswer(studentId,jobId)

    suspend fun commitJobAnswer(jobAnswer: JobAnswer) = RetrofitClient.apiService.CommitJobAnswer(jobAnswer)

    suspend fun deleteJob(jobId: Int) = RetrofitClient.apiService.DeleteJob(jobId)

    suspend fun publishJob(job: Job) = RetrofitClient.apiService.PublishJob(job)

    suspend fun acquireUncheckJob(jobId: Int) = RetrofitClient.apiService.AcquireUncheckedJob(jobId)

    suspend fun commitJobScore(jobId: Int,studentId: Int,score: Double) = RetrofitClient.apiService.CommitJobScore(jobId,studentId,score)

    suspend fun acquireJobDetail(jobId: Int) = RetrofitClient.apiService.AcquireJobDetailForTeacher(jobId)

    suspend fun seekScoreOperation(seekId: Int,score:Int) = RetrofitClient.apiService.SeekScoreOperation(seekId,score)

    suspend fun solveScoreOperation(solveId: Int,score:Int) = RetrofitClient.apiService.SolveScoreOperation(solveId,score)

    suspend fun acquireHelpDetailForTeacher(jobId: Int) = RetrofitClient.apiService.AcquireHelpDetailForTeacher(jobId)


}