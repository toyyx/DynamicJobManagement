package com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2


import com.example.dynamicjobmanagement.model.model.Refreshable
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.databinding.FragmentSquareCenterBinding
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.view.Adapter.HopeSquare.SeekHelpListAdapter
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.PublishAndDetail.HopeDetailActivity
import com.example.dynamicjobmanagement.viewmodel.Repository.CourseRepository
import com.example.dynamicjobmanagement.viewmodel.Repository.HelpRepository
import com.example.dynamicjobmanagement.viewmodel.viewModel.hopeSquareViewModel.SquareCenterViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SquareCenterFragment : Fragment() , SeekHelpListAdapter.OnSeekHelpClickListener, Refreshable{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SeekHelpListAdapter
    private lateinit var viewModel: SquareCenterViewModel
    private lateinit var search_ET: EditText
    private lateinit var refresh_SRL: SwipeRefreshLayout
    var first=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 设置数据绑定
        var binding = DataBindingUtil.inflate<FragmentSquareCenterBinding>(inflater, R.layout.fragment_square_center, container, false)
        // 获取 ViewModel
        viewModel = ViewModelProvider(this).get(SquareCenterViewModel::class.java)
        // 将 ViewModel 绑定到布局
        binding.viewModel = viewModel
        // 允许 LiveData 与布局保持同步
        binding.lifecycleOwner = viewLifecycleOwner

        val spinner=binding.squareCenterCourseSpinner
        // 下拉选项内容
        val options = CourseRepository.getCourseDetailList()?.map { it.courseName }
        // 创建适配器并设置下拉内容
        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, options!!)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // 设置适配器
        spinner.adapter = spinnerAdapter


        search_ET=binding.root.findViewById(R.id.squareCenter_search_EditText)

        // 初始化RecyclerView
        recyclerView = binding.root.findViewById(R.id.squareCenter_seekHelp_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 初始化Adapter
        adapter = SeekHelpListAdapter(this)
        recyclerView.adapter = adapter
//        adapter.setData(viewModel.filteredSeekHelp.value?: listOf())

        refresh_SRL=binding.root.findViewById<SwipeRefreshLayout>(R.id.squareCenter_refresh_SwipeRefreshLayout)
        refresh_SRL.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.acquireSeekHelp()
                viewModel.filterSeekHelp()
                refresh_SRL.isRefreshing = false
            }
        }

//        lifecycleScope.launch {
//            viewModel.acquireSeekHelp()
//            viewModel.filterSeekHelp()
//            adapter.setData(viewModel.filteredSeekHelp.value?: listOf())
//        }

        // 观察ViewModel中的数据变化
        viewModel.acquireSeekHelpResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                if(first){
                    // 更新Adapter中的数据
                    adapter.setData(HelpRepository.getSeekHelpList()!!)
                    first=false
                }

//                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.selectedCoursePosition.observe(this) {
            viewModel.filterSeekHelp()
        }

        viewModel.searchContent.observe(viewLifecycleOwner, Observer { seekHelps ->
            viewModel.filterSeekHelp()
        })

        viewModel.filteredSeekHelp.observe(viewLifecycleOwner, Observer { seekHelps ->
            adapter.setData(seekHelps)
        })


        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        Toast.makeText(requireContext(), "onResume", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            viewModel.acquireSeekHelp()
            delay(500)
            viewModel.filterSeekHelp()
        }
    }

    override fun onSeekHelpClick(seekHelp: SeekHelp) {
        val intent = Intent(requireContext(), HopeDetailActivity::class.java)
        intent.putExtra("seekHelp", seekHelp)
        startActivity(intent)
    }

    override fun refreshData() {
        lifecycleScope.launch {
            viewModel.acquireSeekHelp()
            viewModel.filterSeekHelp()
        }
    }

    companion object {
        fun newInstance(): SquareCenterFragment {
            return SquareCenterFragment()
        }
    }
}