package com.example.dynamicjobmanagement.view.CourseCenter

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Course
import com.example.dynamicjobmanagement.view.Adapter.CourseCenter.CourseListAdapter
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.CourseInformationActivity
import com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel.CourseCenterViewModel
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CourseCenterFragment : Fragment() , CourseListAdapter.OnCourseClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CourseListAdapter
    private lateinit var viewModel: CourseCenterViewModel
    private lateinit var search_ET: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_course_center, container, false)

        // 初始化ViewModel
        viewModel = ViewModelProvider(this).get(CourseCenterViewModel::class.java)

        search_ET=view.findViewById(R.id.courseCenter_search_EditText)

        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.courseCenter_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // 初始化Adapter
        adapter = CourseListAdapter(this)
        recyclerView.adapter = adapter

        // 观察ViewModel中的数据变化
        viewModel.acquireCourseResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                // 更新Adapter中的数据
                adapter.setData(CourseRepository.getCourseDetailList()!!)
                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.filteredCourses.observe(viewLifecycleOwner, Observer { courses ->
            adapter.setData(courses)
        })

        search_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterCourses(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        return view
    }

    override fun onCourseClick(course: Course) {
        val intent = Intent(requireContext(), CourseInformationActivity::class.java)
        intent.putExtra("course", course)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): CourseCenterFragment {
            return CourseCenterFragment()
        }
    }
}