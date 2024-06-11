package com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2

import com.example.dynamicjobmanagement.model.model.Refreshable
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.Job
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.view.Adapter.CourseCenter.CourseJobAdapter
import com.example.dynamicjobmanagement.view.Adapter.CourseCenter.CourseJobAdapter_Teacher
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.TeacherJob.CheckJobActivity
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.StudentJob.JobAnswerActivity
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.TeacherJob.PublishJobActivity
import com.example.dynamicjobmanagement.view.CourseCenter.CourseInfo.viewPager2.TeacherJob.TeacherJobDetailActivity
import com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel.CourseInfoViewModel
import com.example.dynamicjobmanagement.viewmodel.viewModel.courseCenterViewModel.courseInfoViewModel.CourseJobViewModel
import com.example.dynamicjobmanagement.viewmodel.Factory.CourseCenterFactory.CourseJobViewModelFactory
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch


class CourseJobFragment : Fragment() , CourseJobAdapter.OnJobClickListener,
    CourseJobAdapter_Teacher.OnTeacherJobClickListener,Refreshable {

    private val viewModel_activity: CourseInfoViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter_ForStudent: CourseJobAdapter
    private lateinit var adapter_ForTeacher: CourseJobAdapter_Teacher
    private lateinit var viewModel: CourseJobViewModel
    private lateinit var addJob_FAB: FloatingActionButton
    private lateinit var refresh_SRL: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_course_job, container, false)
        addJob_FAB=view.findViewById(R.id.courseJob_addJob_FloatingActionButton)

        // 初始化 ViewModel，传入参数
        viewModel = ViewModelProvider(this, CourseJobViewModelFactory(viewModel_activity.course.value!!.jobList))
            .get(CourseJobViewModel::class.java)

        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.courseJob_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 初始化Adapter
        adapter_ForStudent = CourseJobAdapter(this)
        adapter_ForTeacher = CourseJobAdapter_Teacher(viewModel_activity.course.value!!,this)
        if(UserRepository.getUserType()==UserType.STUDENT){
            recyclerView.adapter = adapter_ForStudent
            addJob_FAB.visibility=View.GONE
        }
        else{
            recyclerView.adapter = adapter_ForTeacher
            addJob_FAB.visibility=View.VISIBLE
        }

        refresh_SRL=view.findViewById<SwipeRefreshLayout>(R.id.courseJob_SwipeRefreshLayout)
        refresh_SRL.setOnRefreshListener {
//            Toast.makeText(requireContext(), "refresh_SRL", Toast.LENGTH_SHORT).show()
            viewModel.refreshCourse()
            refresh_SRL.isRefreshing = false
        }


        // 观察ViewModel中的数据变化
        viewModel.acquireCourseResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                viewModel_activity.course.value=CourseRepository.getCourseDetailList()!!.find{it.courseId == viewModel_activity.course.value!!.courseId}
                viewModel.jobIdList= viewModel_activity.course.value!!.jobList
                viewModel.refreshJob()
//                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                println("error:${exception.message}")
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.acquireJobResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                // 更新Adapter中的数据
                if(UserRepository.getUserType()==UserType.STUDENT)
                    adapter_ForStudent.setData(viewModel.jobList.value!!)
                else
                    adapter_ForTeacher.setData(viewModel.jobList.value!!)
//                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                println("error:${exception.message}")
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        // 观察ViewModel中的数据变化
        viewModel.deleteJobResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                viewModel.refreshCourse()
                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                println("error:${exception.message}")
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.exportJobAnswerToExcelResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                println("error:${exception.message}")
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.exportHelpToExcelResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                println("error:${exception.message}")
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        addJob_FAB.setOnClickListener{
            val intent = Intent(requireContext(), PublishJobActivity::class.java)
            intent.putExtra("courseId", viewModel_activity.course.value!!.courseId)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(requireContext(), "onResume", Toast.LENGTH_SHORT).show()
        viewModel.refreshCourse()
    }

    override fun onStudentJobClick(job: Job) {
        val intent = Intent(requireContext(), JobAnswerActivity::class.java)
        intent.putExtra("job", job)
        startActivity(intent)
    }

    override fun onTeacherJobClick(job: Job) {
        val intent = Intent(requireContext(), TeacherJobDetailActivity::class.java)
        intent.putExtra("job", job)
        startActivity(intent)
    }

    override fun onOperationButtonClick(view: View, job: Job) {
        // 创建一个PopupWindow
        val inflater = requireContext().getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.job_operation_popup_menu, null)
        val popupWindow = PopupWindow(popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true)

        // 获取PopupWindow的选项
        val option1 = popupView.findViewById<TextView>(R.id.jobOperation_updateJob)
        val option2 = popupView.findViewById<TextView>(R.id.jobOperation_checkJob)
        val option3 = popupView.findViewById<TextView>(R.id.jobOperation_deleteJob)
        val option4 = popupView.findViewById<TextView>(R.id.jobOperation_exportJob)
        val option5 = popupView.findViewById<TextView>(R.id.jobOperation_exportHelp)

        // 设置选项点击事件
        option1.setOnClickListener {
            val intent = Intent(requireContext(), PublishJobActivity::class.java)
            intent.putExtra("courseId", job.courseId)
            intent.putExtra("job", job)
            startActivity(intent)
            popupWindow.dismiss()
        }

        option2.setOnClickListener {
            val intent = Intent(requireContext(), CheckJobActivity::class.java)
            intent.putExtra("job", job)
            startActivity(intent)
            popupWindow.dismiss()
        }

        option3.setOnClickListener {
            showDeleteJobConfirmDialog(requireContext(),job)
            popupWindow.dismiss()
        }

        option4.setOnClickListener {
            viewModel.acquireJobDetail(requireContext(),job.jobId,"${job.jobTitle}-作业完成情况",job.jobTitle)
        }

        option5.setOnClickListener {
            viewModel.acquireHelpDetail(requireContext(),job.jobId,"${job.jobTitle}-作业拼情况")
        }

        // 显示PopupWindow在按钮的左侧
        popupWindow.showAsDropDown(view, -view.width, 0)
    }

    fun showDeleteJobConfirmDialog(context: Context, job: Job){
        AlertDialog.Builder(context).apply {
            setTitle("删除作业")
            setMessage("确定删除这个作业？")
            setPositiveButton("是") { dialog, _ ->
                viewModel.deleteJob(job.jobId)
                dialog.dismiss()
            }
            setNegativeButton("否") { dialog, _ ->
                // 用户点击了 "否"，关闭对话框
                dialog.dismiss()
            }
            create()
            show()
        }
    }

    override fun refreshData() {
        if(::viewModel.isInitialized){
//            Toast.makeText(requireContext(), "正在刷新数据", Toast.LENGTH_SHORT).show()
            viewModel.refreshCourse()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CourseJobFragment().apply {

            }
    }
}