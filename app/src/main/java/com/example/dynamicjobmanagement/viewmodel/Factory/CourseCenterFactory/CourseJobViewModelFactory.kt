package com.example.dynamicjobmanagement.viewmodel.Factory.CourseCenterFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel.CourseJobViewModel

class CourseJobViewModelFactory(private val jobIdList:List<String>?): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseJobViewModel::class.java)) {
            return CourseJobViewModel(jobIdList) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}