package com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2

import com.example.dynamicjobmanagement.model.model.Refreshable
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.SeekHelp
import com.example.dynamicjobmanagement.view.Adapter.HopeSquare.MySquareSeekHelpListAdapter
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.PublishAndDetail.HopeDetailActivity
import com.example.dynamicjobmanagement.view.HopeSqure.ViewPager2.PublishAndDetail.PublishHopeActivity
import com.example.dynamicjobmanagement.viewmodel.viewModel.hopeSquareViewModel.MySquareViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MySquareFragment : Fragment(), MySquareSeekHelpListAdapter.OnMySeekHelpClickListener, Refreshable{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MySquareSeekHelpListAdapter
    private lateinit var viewModel: MySquareViewModel
    private lateinit var refresh_SRL: SwipeRefreshLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_my_square, container, false)

        // 初始化ViewModel
        viewModel = ViewModelProvider(this).get(MySquareViewModel::class.java)

        // 初始化RecyclerView
        recyclerView = view.findViewById(R.id.mySquare_seekHelp_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        // 初始化Adapter
        adapter = MySquareSeekHelpListAdapter(this)
        recyclerView.adapter = adapter

        refresh_SRL=view.findViewById<SwipeRefreshLayout>(R.id.mySquare_SwipeRefreshLayout)
        refresh_SRL.setOnRefreshListener {
            viewModel.refreshMySeekHelp()
            refresh_SRL.isRefreshing = false
        }

        // 观察ViewModel中的数据变化
        viewModel.acquireMySeekHelpResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                // 更新Adapter中的数据
//                adapter.setData(viewModel.MyseekHelpList.value!!)
//                Toast.makeText(requireContext(), info, Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.MyseekHelpList.observe(this, Observer {
            adapter.setData(it)
        })

        view.findViewById<FloatingActionButton>(R.id.mySquare_AddSeekHelp_FloatingActionButton).setOnClickListener {
            val intent = Intent(requireContext(), PublishHopeActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshMySeekHelp()
    }

    override fun onSeekHelpClick(seekHelp: SeekHelp) {
        val intent = Intent(requireContext(), HopeDetailActivity::class.java)
        intent.putExtra("seekHelp", seekHelp)
        startActivity(intent)
    }

    override fun refreshData() {
        viewModel.refreshMySeekHelp()
    }

    companion object {
        fun newInstance(): MySquareFragment {
            return MySquareFragment()
        }
    }
}