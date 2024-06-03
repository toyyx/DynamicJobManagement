package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.ViewPager2

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.view.Adapter.CourseCenter.CourseMemberAdapter
import com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseInfoViewModel.CourseInfoViewModel
import com.example.dynamicjobmanagement.viewmodel.ViewModel.CourseCenterViewModel.CourseMemberViewModel
import com.example.dynamicjobmanagement.viewmodel.Factory.CourseCenterFactory.CourseMemberViewModelFactory


class CourseMemberFragment : Fragment() {

    private val viewModel_activity: CourseInfoViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CourseMemberAdapter
    private lateinit var search_ET: EditText
    private lateinit var viewModel: CourseMemberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_course_member, container, false)

        // 初始化 ViewModel，传入参数
        viewModel = ViewModelProvider(this, CourseMemberViewModelFactory(viewModel_activity.course.value!!.studentList))
            .get(CourseMemberViewModel::class.java)

        search_ET=view.findViewById(R.id.courseMember_search_EditText)

        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.courseMember_member_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // 初始化Adapter
        adapter = CourseMemberAdapter()
        recyclerView.adapter = adapter

        // 观察ViewModel中的数据变化
        viewModel.acquireMemberResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                // 更新Adapter中的数据
                adapter.setData(viewModel.filteredMembers.value!!)
                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.filteredMembers.observe(viewLifecycleOwner, Observer { members ->
            adapter.setData(members)
        })

        search_ET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.filterMembers(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })


        return view
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseMemberFragment().apply {

            }
    }
}