package com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.TeacherJobViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.JobAnswer
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class CheckJobViewModel: ViewModel() {
    val acquireUncheckedJobResult = MutableLiveData<Result<String?>>()
    val saveJobScoreResult = MutableLiveData<Result<String?>>()

    val uncheckedJobAnswerList = MutableLiveData<List<JobAnswer>>()

    val nowPosition=MutableLiveData<Int>()

    val studentAnswer=MutableLiveData<String>()
    val score=MutableLiveData<String>()

    private var nowCheckJobId:Int?=null

    fun initCheckJobId(jobId: Int){
        nowCheckJobId=jobId
        acquireUncheckJob()
    }

    fun acquireUncheckJob(){
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireUncheckJob(nowCheckJobId!!)
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val jobAnswerJsonArray=jsonObject.getAsJsonArray("uncheckedJobInfo")
                        val jobAnswerListType = object : TypeToken<List<JobAnswer>>() {}.type
                        uncheckedJobAnswerList.value=Gson().fromJson(jobAnswerJsonArray, jobAnswerListType)
                        if(uncheckedJobAnswerList.value!!.isEmpty()){
                            nowPosition.value=-1
                        }else
                            nowPosition.value=0
                        acquireUncheckedJobResult.postValue(Result.success("待批改作业信息获取成功"))
                    }else
                        acquireUncheckedJobResult.postValue(Result.failure(Exception("待批改作业信息获取失败")))
                }else
                    acquireUncheckedJobResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireUncheckedJobResult.postValue(Result.failure(e))
            }
        }
    }

    fun saveScore(){
        viewModelScope.launch {
            try {
                val response = CourseRepository.commitJobScore(uncheckedJobAnswerList.value!![nowPosition.value!!].jobId,uncheckedJobAnswerList.value!![nowPosition.value!!].studentId,score.value!!.toDouble())
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        if(nowPosition.value!!+1==uncheckedJobAnswerList.value!!.size){
                            nowPosition.value=-1
                        }else{
                            nowPosition.value = nowPosition.value!! + 1
                        }
                        saveJobScoreResult.postValue(Result.success("保存评分成功"))
                    }else
                        saveJobScoreResult.postValue(Result.failure(Exception("保存评分失败")))
                }else
                    saveJobScoreResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                saveJobScoreResult.postValue(Result.failure(e))
            }
        }
    }

    fun saveAndNext(){
        if(studentAnswer.value.isNullOrBlank()){
            saveJobScoreResult.postValue(Result.failure(Exception("暂无作业需要批改")))
        }else if(score.value!!.toDoubleOrNull()==null){
            saveJobScoreResult.postValue(Result.failure(Exception("请填写正确评分内容")))
        }else{
            saveScore()
        }
    }
}