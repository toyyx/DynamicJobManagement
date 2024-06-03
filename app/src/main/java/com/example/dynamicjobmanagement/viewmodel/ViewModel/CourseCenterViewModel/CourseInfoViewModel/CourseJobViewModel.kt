package com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.model.model.JobAnswer
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.model.model.SolveHelp
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

class CourseJobViewModel(jobIdList:List<String>?): ViewModel() {
    val acquireJobResult = MutableLiveData<Result<String?>>()
    val jobList = MutableLiveData<List<Job>>()
    val deleteJobResult = MutableLiveData<Result<String?>>()
    val exportJobAnswerToExcelResult = MutableLiveData<Result<String?>>()
    val exportHelpToExcelResult = MutableLiveData<Result<String?>>()
    init{
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireCourseJobs(jobIdList)
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        //获取课程详细信息
                        val jobJsonArray=jsonObject.getAsJsonArray("jobInfo")
                        val jobListType = object : TypeToken<List<Job>>() {}.type
                        jobList.value= Gson().fromJson(jobJsonArray, jobListType)
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
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
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
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        exportJobAnswerToExcelResult.postValue(Result.success("作业情况获取成功"))
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
            val directory = File(context.filesDir, "ExportFile")
            // 如果目录不存在，则创建它
            if (!directory.exists()) {
                directory.mkdirs()
            }
            // 创建 Excel 文件
            val filePath = File(directory, "${excelName}.xlsx").absolutePath

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
                row.createCell(2).setCellValue(detail.score)
            }

            // 调整列宽
            sheet.autoSizeColumn(0)
            sheet.autoSizeColumn(1)

            // 写入文件
            val fileOut = FileOutputStream(filePath)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
            exportJobAnswerToExcelResult.postValue(Result.success("文件导出成功，文件位置:${filePath}"))
        }catch (e: Exception) {
            exportJobAnswerToExcelResult.postValue(Result.failure(e))
        }
    }

    fun acquireHelpDetail(context: Context, jobId: Int, excelName:String){
        viewModelScope.launch {
            try {
                val response = CourseRepository.acquireHelpDetailForTeacher(jobId)
                println("response:${response}")
                if (response.isSuccessful && response.body() != null) {
                    val jsonObject = response.body()
                    // 读取特定键的值
                    val result = jsonObject!!.get("result")!!.asString
                    if (result == "success") {
                        exportHelpToExcelResult.postValue(Result.success("作业拼情况获取成功"))
                        //获取课程详细信息
                        val seekHelpJsonArray=jsonObject.getAsJsonArray("seekHelpInfo")
                        val seekHelpListType = object : TypeToken<List<SeekHelp>>() {}.type
                        val solveHelpJsonArray=jsonObject.getAsJsonArray("solveHelpInfo")
                        val solveHelpListType = object : TypeToken<List<SolveHelp>>() {}.type
                        exportHelpToExcel(context,Gson().fromJson(seekHelpJsonArray, seekHelpListType),Gson().fromJson(solveHelpJsonArray, solveHelpListType),excelName)
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
            val directory = File(context.filesDir, "ExportFile")
            // 如果目录不存在，则创建它
            if (!directory.exists()) {
                directory.mkdirs()
            }
            // 创建 Excel 文件
            val filePath = File(directory, "${excelName}.xlsx").absolutePath

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
                row.createCell(1).setCellValue(detail.publishTime)
                row.createCell(2).setCellValue(detail.seekerName)
                row.createCell(3).setCellValue(detail.seekContent)
                row.createCell(4).setCellValue(detail.likeNumber.toString())
                row.createCell(5).setCellValue(detail.commentNumber.toString())
                row.createCell(6).setCellValue(detail.score.toString())
            }

            // 调整列宽
            sheet1.autoSizeColumn(0)
            sheet1.autoSizeColumn(1)
            sheet1.autoSizeColumn(2)
            sheet1.autoSizeColumn(3)
            sheet1.autoSizeColumn(4)
            sheet1.autoSizeColumn(5)
            sheet1.autoSizeColumn(6)


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
                row.createCell(2).setCellValue(detail.replyTime)
                row.createCell(3).setCellValue(detail.replierName)
                row.createCell(4).setCellValue(detail.replyContent)
                row.createCell(5).setCellValue(detail.score.toString())
            }

            // 调整列宽
            sheet2.autoSizeColumn(0)
            sheet2.autoSizeColumn(1)
            sheet2.autoSizeColumn(2)
            sheet2.autoSizeColumn(3)
            sheet2.autoSizeColumn(4)
            sheet2.autoSizeColumn(5)

            // 写入文件
            val fileOut = FileOutputStream(filePath)
            workbook.write(fileOut)
            fileOut.close()
            workbook.close()
            exportHelpToExcelResult.postValue(Result.success("文件导出成功，文件位置:${filePath}"))
        }catch (e: Exception) {
            exportHelpToExcelResult.postValue(Result.failure(e))
        }

    }

}