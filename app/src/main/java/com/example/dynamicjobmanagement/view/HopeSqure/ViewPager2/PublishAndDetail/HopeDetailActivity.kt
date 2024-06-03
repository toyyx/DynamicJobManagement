package com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.PublishAndDetail

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.model.model.SolveHelp
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.view.Adapter.HopeSquare.SolveHelpListAdapter
import com.example.dynamicjobmanagement.viewmodel.ViewModel.HopeSquareViewModel.HopeDetailViewModel
import com.example.dynamicjobmanagement.viewmodel.Factory.HopeSquareFactory.HopeDetailViewModelFactory
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import java.time.LocalDateTime

class HopeDetailActivity : AppCompatActivity() , SolveHelpListAdapter.OnHelpOperationClickListener{
    private lateinit var course_job_TV: TextView
    private lateinit var time_TV: TextView
    private lateinit var seekerName_ET: TextView
    private lateinit var seekContent: TextView
    private lateinit var likeNum_TV: TextView
    private lateinit var commentNum_TV: TextView
    private lateinit var seekHelpOperation_IB: ImageButton

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SolveHelpListAdapter
    private lateinit var viewModel: HopeDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hope_detail)

        course_job_TV=findViewById(R.id.hopeDetail_course_job_TextView)
        time_TV=findViewById(R.id.hopeDetail_time_TextView)
        seekerName_ET=findViewById(R.id.hopeDetail_seekerName_TextView)
        seekContent=findViewById(R.id.hopeDetail_content_TextView)
        likeNum_TV=findViewById(R.id.hopeDetail_likeNum_TextView)
        commentNum_TV=findViewById(R.id.hopeDetail_commentNum_TextView)
        seekHelpOperation_IB=findViewById(R.id.hopeDetail_dealOperation_ImageButton)

        if(UserRepository.getUserType()==UserType.STUDENT)
            seekHelpOperation_IB.visibility=View.GONE
        else
            seekHelpOperation_IB.visibility=View.VISIBLE


        // 接收传递的数据
        val seekHelp = intent.getSerializableExtra("seekHelp") as SeekHelp

        course_job_TV.text="${seekHelp.courseName}-${seekHelp.jobTitle}"

        val publishTime=seekHelp.publishTime
        val nowDateTime= LocalDateTime.now()
        if(publishTime.year==nowDateTime.year&&publishTime.month==nowDateTime.month){
            when(publishTime.dayOfMonth-nowDateTime.dayOfMonth){
                0 -> time_TV.text = "今天 ${publishTime.toLocalTime()}"
                -1 -> time_TV.text = "昨天 ${publishTime.toLocalTime()}"
                -2 -> time_TV.text = "前天 ${publishTime.toLocalTime()}"
                else ->time_TV.text = publishTime.toString()
            }
        }else
            time_TV.text = publishTime.toString()

        seekerName_ET.text=seekHelp.seekerName
        seekContent.text=seekHelp.seekContent
        likeNum_TV.text=seekHelp.likeNumber.toString()
        commentNum_TV.text=seekHelp.commentNumber.toString()

        // 初始化 ViewModel，传入参数
        viewModel = ViewModelProvider(this, HopeDetailViewModelFactory(seekHelp)).get(
            HopeDetailViewModel::class.java)

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.hopeDetail_solveHelp_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // 初始化Adapter
        adapter = SolveHelpListAdapter(this)
        recyclerView.adapter = adapter

        // 观察ViewModel中的数据变化
        viewModel.acquireSolveHelpResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                findViewById<TextView>(R.id.hopeDetail_solveNum_TextView).text="帮助记录(${viewModel.solveHelpList.value!!.size})"
                // 更新Adapter中的数据
                adapter.setData(viewModel.solveHelpList.value!!)
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.solveHelpList.observe(this, Observer {
            // 更新Adapter中的数据
            adapter.setData(viewModel.solveHelpList.value!!)
        })

        // 观察ViewModel中的数据变化
        viewModel.addLikeResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                findViewById<ImageView>(R.id.hopeDetail_addLike_ImageView).setBackgroundColor(Color.RED)
                seekHelp.likeNumber++
                likeNum_TV.text=seekHelp.likeNumber.toString()
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        // 观察ViewModel中的数据变化
        viewModel.addCommentResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                seekHelp.commentNumber++
                commentNum_TV.text=seekHelp.commentNumber.toString()
                findViewById<TextView>(R.id.hopeDetail_solveNum_TextView).text="帮助记录(${seekHelp.commentNumber})"
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.seekScoreOperationResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.solveScoreOperationResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })



        findViewById<ImageView>(R.id.hopeDetail_back_ImageView).setOnClickListener {
            finish()
        }

        findViewById<ImageView>(R.id.hopeDetail_addLike_ImageView).setOnClickListener {
            viewModel.onAddLikeClike(seekHelp.seekId)
        }

        findViewById<ImageView>(R.id.hopeDetail_prepareComment_ImageView).setOnClickListener {
            showEditCommentWindow(this,seekHelp.seekId)
        }

        seekHelpOperation_IB.setOnClickListener {
            onOperationClick(it,seekHelp)
        }


    }

    fun showEditCommentWindow(context: Context,seekHelpId: Int){
        // 创建一个AlertDialog.Builder对象
        val builder = AlertDialog.Builder(context)
        builder.setTitle("参与作业拼")

        // 创建一个EditText对象并添加到对话框中
        val input = EditText(context)
        input.hint = "请输入你的解决方法"
        builder.setView(input)

        // 设置确定按钮及其点击事件
        builder.setPositiveButton("提交") { dialog, which ->
            val text = input.text.toString()
            if(text.isEmpty()){
                Toast.makeText(this, "内容不可为空", Toast.LENGTH_SHORT).show()
            }else{
                if(UserRepository.getUserType()==UserType.STUDENT){
                    viewModel.onAddCommentClike(SolveHelp(0,
                        seekHelpId,
                        UserRepository.getStudentUser()!!.studentId,
                        UserRepository.getStudentUser()!!.name,
                        text,
                        LocalDateTime.now(),
                        0
                    ))
                }else{
                    viewModel.onAddCommentClike(SolveHelp(0,
                        seekHelpId,
                        UserRepository.getTeacherUser()!!.teacherId,
                        UserRepository.getTeacherUser()!!.name,
                        text,
                        LocalDateTime.now(),
                        0
                    ))
                }
                dialog.cancel()
            }
        }

        // 设置取消按钮及其点击事件
        builder.setNegativeButton("取消") { dialog, which ->
            // 在这里处理取消按钮的点击事件，比如关闭对话框
            dialog.cancel()
        }

        // 创建并显示AlertDialog
        val dialog = builder.create()
        dialog.show()

    }

    override fun onOperationClick(view:View, solveHelp: SolveHelp) {
        // 创建一个PopupWindow
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.help_score_operation_popup_menu, null)
        val popupWindow = PopupWindow(popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true)

        // 获取PopupWindow的选项
        val option1 = popupView.findViewById<TextView>(R.id.helpScoreOperation_add3)
        val option2 = popupView.findViewById<TextView>(R.id.helpScoreOperation_add1)
        val option3 = popupView.findViewById<TextView>(R.id.helpScoreOperation_lose5)

        // 设置选项点击事件
        option1.setOnClickListener {
            viewModel.solveScoreOperation(solveHelp.solveId,3)
            popupWindow.dismiss()
        }

        option2.setOnClickListener {
            viewModel.solveScoreOperation(solveHelp.solveId,1)
            popupWindow.dismiss()
        }

        option3.setOnClickListener {
            viewModel.solveScoreOperation(solveHelp.solveId,-5)
            popupWindow.dismiss()
        }

        // 显示PopupWindow在按钮的左侧
        popupWindow.showAsDropDown(view, -view.width, 0)
    }

    fun onOperationClick(view:View, seekHelp: SeekHelp) {
        // 创建一个PopupWindow
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.help_score_operation_popup_menu, null)
        val popupWindow = PopupWindow(popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true)

        // 获取PopupWindow的选项
        val option1 = popupView.findViewById<TextView>(R.id.helpScoreOperation_add3)
        val option2 = popupView.findViewById<TextView>(R.id.helpScoreOperation_add1)
        val option3 = popupView.findViewById<TextView>(R.id.helpScoreOperation_lose5)

        // 设置选项点击事件
        option1.setOnClickListener {
            viewModel.seekScoreOperation(seekHelp.seekId,3)
            popupWindow.dismiss()
        }

        option2.setOnClickListener {
            viewModel.seekScoreOperation(seekHelp.seekId,1)
            popupWindow.dismiss()
        }

        option3.setOnClickListener {
            viewModel.seekScoreOperation(seekHelp.seekId,-5)
            popupWindow.dismiss()
        }

        // 显示PopupWindow在按钮的左侧
        popupWindow.showAsDropDown(view, -view.width, 0)
    }


}