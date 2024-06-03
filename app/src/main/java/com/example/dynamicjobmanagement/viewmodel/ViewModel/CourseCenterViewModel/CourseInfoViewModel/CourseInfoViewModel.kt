package com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamicjobmanagement.model.model.Course

class CourseInfoViewModel: ViewModel() {
    val course: MutableLiveData<Course> = MutableLiveData()


}