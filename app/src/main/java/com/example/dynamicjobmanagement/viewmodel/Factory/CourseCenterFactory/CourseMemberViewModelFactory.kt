package com.example.dynamicjobmanagement.viewmodel.Factory.CourseCenterFactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.CourseMemberViewModel

class CourseMemberViewModelFactory(private val memberIdList:List<String>?): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CourseMemberViewModel::class.java)) {
            return CourseMemberViewModel(memberIdList) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}