package com.example.dynamicjobmanagement.viewmodel.Factory.CourseCenterFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.StudentJobViewModel.JobAnswerViewModel

class JobAnswerViewModelFactory(private val job: Job): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JobAnswerViewModel::class.java)) {
            return JobAnswerViewModel(job) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}