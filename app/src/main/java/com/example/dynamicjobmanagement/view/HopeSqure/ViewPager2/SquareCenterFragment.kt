package com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.databinding.FragmentSquareCenterBinding
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.view.Adapter.HopeSquare.SeekHelpListAdapter
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.PublishAndDetail.HopeDetailActivity
import com.example.dynamicjobmanagement.viewmodel.ViewModel.HopeSquareViewModel.SquareCenterViewModel


class SquareCenterFragment : Fragment() , SeekHelpListAdapter.OnSeekHelpClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SeekHelpListAdapter
    private lateinit var viewModel: SquareCenterViewModel
    private lateinit var search_ET: EditText

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


        search_ET=binding.root.findViewById(R.id.squareCenter_search_EditText)

        // 初始化RecyclerView
        recyclerView = binding.root.findViewById(R.id.squareCenter_seekHelp_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // 初始化Adapter
        adapter = SeekHelpListAdapter(this)
        recyclerView.adapter = adapter

        // 观察ViewModel中的数据变化
        viewModel.acquireSeekHelpResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {info ->
                // 更新Adapter中的数据
                adapter.setData(viewModel.filteredSeekHelp.value!!)
                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
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

    override fun onSeekHelpClick(seekHelp: SeekHelp) {
        val intent = Intent(requireContext(), HopeDetailActivity::class.java)
        intent.putExtra("seekHelp", seekHelp)
        startActivity(intent)
    }

    companion object {
        fun newInstance(): SquareCenterFragment {
            return SquareCenterFragment()
        }
    }
}