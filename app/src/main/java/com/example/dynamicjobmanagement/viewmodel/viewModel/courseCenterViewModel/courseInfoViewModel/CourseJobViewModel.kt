package com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel

import LocalDateTimeAdapter
import android.content.Context
import android.os.Environment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Course
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.model.model.JobAnswer
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.model.model.SolveHelp
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime

class CourseJobViewModel(jobIdList_init:List<String>?): ViewModel() {
    val acquireCourseResult = MutableLiveData<Result<String?>>()
    val acquireJobResult = MutableLiveData<Result<String?>>()
    val jobList = MutableLiveData<List<Job>>()
    val deleteJobResult = MutableLiveData<Result<String?>>()
    val exportJobAnswerToExcelResult = MutableLiveData<Result<String?>>()
    val exportHelpToExcelResult = MutableLiveData<Result<String?>>()
    var jobIdList:List<String>?

    init{
        this.jobIdList=jobIdList_init
        refreshJob()
    }

    fun refreshJob(){
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireCourseJobs(jobIdList)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val jobJsonArray=jsonObject.getAsJsonArray("jobInfo")
                        val newJsonArray= JsonArray()
                        jobJsonArray.forEachIndexed  { index, element ->
                            if (element.isJsonObject) {
                                // 如果元素是 JsonObject 类型，则进行处理
                                val jsonO = element.asJsonObject
                                // 获取 startTime 字段的值，并去掉两侧的引号
                                val startTime = jsonO.get("startTime").asString.trim('"')
                                val endTime = jsonO.get("endTime").asString.trim('"')
                                // 修改 jsonObject 中的 startTime 字段的值为去掉引号后的值
                                jsonO.addProperty("startTime", startTime)
                                jsonO.addProperty("endTime", endTime)
                                newJsonArray.add(jsonO)
                            }
                        }

                        // 创建 GsonBuilder 实例，并注册 LocalDateTimeAdapter
                        val gson = GsonBuilder()
                            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                            .create()
                        val jobListType = object : TypeToken<List<Job>>() {}.type
                        jobList.value= gson.fromJson(newJsonArray, jobListType)
                        acquireJobResult.postValue(Result.success("课程作业信息获取成功"))
                    }else
                        acquireJobResult.postValue(Result.failure(Exception("课程作业信息获取失败")))
                }else
                    acquireJobResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireJobResult.postValue(Result.failure(e))
            }
        }
    }



    fun deleteJob(jobId:Int){
        viewModelScope.launch {
            try {
                val response = CourseRepository.deleteJob(jobId)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        // 获取当前列表的值
                        val currentList = jobList.value
                        // 创建一个新的列表并删除指定ID的元素
                        val updatedList = currentList!!.toMutableList().apply {
                            removeIf { it.jobId == jobId }
                        }
                        // 将新列表设置为MutableLiveData的值
                        jobList.value = updatedList
                        deleteJobResult.postValue(Result.success("作业删除成功"))
                    }else
                        deleteJobResult.postValue(Result.failure(Exception("作业删除失败")))
                }else
                    deleteJobResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                deleteJobResult.postValue(Result.failure(e))
            }
        }
    }

    fun acquireJobDetail(context: Context, jobId: Int, excelName:String, sheetTitle:String){
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireJobDetail(jobId)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
//                        exportJobAnswerToExcelResult.postValue(Result.success("作业情况获取成功"))
                        //获取课程详细信息
                        val jobAnswerJsonArray=jsonObject.getAsJsonArray("jobDetailInfo")
                        val jobAnswerListType = object : TypeToken<List<JobAnswer>>() {}.type
                        exportJobAnswerToExcel(context,Gson().fromJson(jobAnswerJsonArray, jobAnswerListType),excelName,sheetTitle)
                    }else
                        exportJobAnswerToExcelResult.postValue(Result.failure(Exception("作业情况获取失败")))
                }else
                    exportJobAnswerToExcelResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                exportJobAnswerToExcelResult.postValue(Result.failure(e))
            }
        }
    }

    fun exportJobAnswerToExcel(context: Context, data: List<JobAnswer>, excelName:String, sheetTitle:String) {
        try{
            // 获取应用的私有目录路径
            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "DJM_ExportFile")
            // 如果目录不存在，则创建它
            if (!directory.exists()) {
                directory.mkdirs()
            }
            // 创建 Excel 文件
            val file = File(directory, "$excelName.xlsx")
            val filePath = file.absolutePath
//            // 创建 Excel 文件
//            val filePath = File(directory, "${excelName}.xlsx").absolutePath

            // 创建 Excel 工作簿
            val workbook = XSSFWorkbook()
            // 创建工作表
            val sheet = workbook.createSheet(sheetTitle)

            // 创建标题行样式
            val headerStyle = workbook.createCellStyle()
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex())
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
            val headerFont = workbook.createFont()
            headerFont.color = IndexedColors.BLACK.getIndex()
            headerStyle.setFont(headerFont)

            // 创建标题行
            val headerRow = sheet.createRow(0)
            val headers = arrayOf("学生姓名", "作答内容","作业得分")
            headers.forEachIndexed { index, header ->
                val cell = headerRow.createCell(index)
                cell.setCellValue(header)
                cell.cellStyle = headerStyle
            }

            // 填充数据
            data.forEachIndexed { rowIndex, detail ->
                val row = sheet.createRow(rowIndex + 1)
                row.createCell(0).setCellValue(detail.studentName)
                row.createCell(1).setCellValue(detail.answer)
                row.createCell(2).setCellValue(detail.score ?: 0.0)
            }

            // 调整列宽
//            sheet.autoSizeColumn(0)
//            sheet.autoSizeColumn(1)

            // 写入文件
            val fileOut = FileOutputStream(filePath)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()

//            exportJobAnswerToExcelResult.postValue(Result.success("文件导出成功，文件位置:${filePath}"))
            println("文件导出成功，文件位置:${filePath}")
            // 检查文件是否创建成功
            if (file.exists()) {
                val successMessage = "文件导出成功，文件位置: ${filePath}"
                exportJobAnswerToExcelResult.postValue(Result.success(successMessage))

            } else {
                val errorMessage = "文件导出失败，文件未找到: ${filePath}"
                exportJobAnswerToExcelResult.postValue(Result.failure(Exception(errorMessage)))
            }
        }catch (e: Exception) {
            exportJobAnswerToExcelResult.postValue(Result.failure(e))
            println("error:$e")
        }
    }

    fun acquireHelpDetail(context: Context, jobId: Int, excelName:String){
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireHelpDetailForTeacher(jobId)
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
//                        exportHelpToExcelResult.postValue(Result.success("作业拼情况获取成功"))
                        // 创建 GsonBuilder 实例，并注册 LocalDateTimeAdapter
                        val gson = GsonBuilder()
                            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
                            .create()
                        //获取课程详细信息
                        val seekHelpJsonArray=jsonObject.getAsJsonArray("seekHelpInfo")
                        val seekHelpListType = object : TypeToken<List<SeekHelp>>() {}.type
                        val solveHelpJsonArray=jsonObject.getAsJsonArray("solveHelpInfo")
                        val solveHelpListType = object : TypeToken<List<SolveHelp>>() {}.type
                        exportHelpToExcel(context,gson.fromJson(seekHelpJsonArray, seekHelpListType),gson.fromJson(solveHelpJsonArray, solveHelpListType),excelName)
                    }else
                        exportHelpToExcelResult.postValue(Result.failure(Exception("作业拼情况获取失败")))
                }else
                    exportHelpToExcelResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                exportHelpToExcelResult.postValue(Result.failure(e))
            }
        }
    }

    fun exportHelpToExcel(context: Context, dataList1: List<SeekHelp>,dataList2: List<SolveHelp>, excelName:String) {
        try{
            // 获取应用的私有目录路径
            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "DJM_ExportFile")
            // 如果目录不存在，则创建它
            if (!directory.exists()) {
                directory.mkdirs()
            }
            // 创建 Excel 文件
            val file = File(directory, "$excelName.xlsx")
            val filePath = file.absolutePath

            // 创建 Excel 工作簿
            val workbook = XSSFWorkbook()
            // 创建工作表
            val sheet1 = workbook.createSheet("发起情况")

            // 创建标题行样式
            val headerStyle = workbook.createCellStyle()
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex())
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)
            val headerFont = workbook.createFont()
            headerFont.color = IndexedColors.BLACK.getIndex()
            headerStyle.setFont(headerFont)

            // 创建标题行
            val headerRow1 = sheet1.createRow(0)
            val headers1 = arrayOf("发起编号（唯一）","发布时间","发布者姓名", "问题描述","点赞数","参与数","奖惩得分")
            headers1.forEachIndexed { index, header ->
                val cell = headerRow1.createCell(index)
                cell.setCellValue(header)
                cell.cellStyle = headerStyle
            }

            // 填充数据
            dataList1.forEachIndexed { rowIndex, detail ->
                val row = sheet1.createRow(rowIndex + 1)
                row.createCell(0).setCellValue(detail.seekId.toString())
                row.createCell(1).setCellValue(detail.publishTime.toString())
                row.createCell(2).setCellValue(detail.seekerName)
                row.createCell(3).setCellValue(detail.seekContent)
                row.createCell(4).setCellValue(detail.likeNumber.toString())
                row.createCell(5).setCellValue(detail.commentNumber.toString())
                row.createCell(6).setCellValue(detail.score.toString())
            }

//            // 调整列宽
//            sheet1.autoSizeColumn(0)
//            sheet1.autoSizeColumn(1)
//            sheet1.autoSizeColumn(2)
//            sheet1.autoSizeColumn(3)
//            sheet1.autoSizeColumn(4)
//            sheet1.autoSizeColumn(5)
//            sheet1.autoSizeColumn(6)


            // 创建表格2
            val sheet2 = workbook.createSheet("参与情况")

            // 创建标题行
            val headerRow2 = sheet2.createRow(0)
            val headers2 = arrayOf("参与编号（唯一）","对应发起编号","参与时间","参与者姓名", "参与内容","奖惩得分")
            headers2.forEachIndexed { index, header ->
                val cell = headerRow2.createCell(index)
                cell.setCellValue(header)
                cell.cellStyle = headerStyle
            }

            // 填充数据
            dataList2.forEachIndexed { rowIndex, detail ->
                val row = sheet2.createRow(rowIndex + 1)
                row.createCell(0).setCellValue(detail.solveId.toString())
                row.createCell(1).setCellValue(detail.seekId.toString())
                row.createCell(2).setCellValue(detail.replyTime.toString())
                row.createCell(3).setCellValue(detail.replierName)
                row.createCell(4).setCellValue(detail.replyContent)
                row.createCell(5).setCellValue(detail.score.toString())
            }

//            // 调整列宽
//            sheet2.autoSizeColumn(0)
//            sheet2.autoSizeColumn(1)
//            sheet2.autoSizeColumn(2)
//            sheet2.autoSizeColumn(3)
//            sheet2.autoSizeColumn(4)
//            sheet2.autoSizeColumn(5)

            // 写入文件
            val fileOut = FileOutputStream(filePath)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
            // 检查文件是否创建成功
            if (file.exists()) {
                val successMessage = "文件导出成功，文件位置: ${filePath}"
                exportHelpToExcelResult.postValue(Result.success(successMessage))

            } else {
                val errorMessage = "文件导出失败，文件未找到: ${filePath}"
                exportHelpToExcelResult.postValue(Result.failure(Exception(errorMessage)))
            }
        }catch (e: Exception) {
            exportHelpToExcelResult.postValue(Result.failure(e))
        }

    }

    fun refreshCourse(){
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireCourse()
                println("response:${response}")
                println("response body:${response.body()}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = Gson().fromJson(response.body(), JsonObject::class.java)
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val courseJsonArray=jsonObject.getAsJsonArray("courseInfo")
                        val courseListType = object : TypeToken<List<Course>>() {}.type
                        CourseRepository.setCourseList(Gson().fromJson(courseJsonArray, courseListType))
                        acquireCourseResult.postValue(Result.success("课程信息获取成功"))
                    }else
                        acquireCourseResult.postValue(Result.failure(Exception("课程信息获取失败")))
                }else
                    acquireCourseResult.postValue(Result.failure(Exception("服务器响应异常")))
            }catch (e: Exception) {
                acquireCourseResult.postValue(Result.failure(e))
            }
        }
    }

}