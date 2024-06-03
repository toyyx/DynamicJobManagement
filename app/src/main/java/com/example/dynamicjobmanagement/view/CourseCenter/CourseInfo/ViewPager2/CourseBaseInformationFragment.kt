package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.ViewPager2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.CourseInfoViewModel

class CourseBaseInformationFragment : Fragment() {
    private val viewModel: CourseInfoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_course_base_information, container, false)

        view.findViewById<TextView>(R.id.courseBaseInfo_courseName_TextView).text=viewModel.course.value?.courseName
        view.findViewById<TextView>(R.id.courseBaseInfo_courseType_TextView).text=viewModel.course.value?.courseType
        view.findViewById<TextView>(R.id.courseBaseInfo_credit_TextView).text= viewModel.course.value?.credit.toString()
        view.findViewById<TextView>(R.id.courseBaseInfo_courseTime_TextView).text=viewModel.course.value?.courseTime
        view.findViewById<TextView>(R.id.courseBaseInfo_courseAddress_TextView).text=viewModel.course.value?.courseAddress

        return view
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseBaseInformationFragment().apply {

            }
    }
}