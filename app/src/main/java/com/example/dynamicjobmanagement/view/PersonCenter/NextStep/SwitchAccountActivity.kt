package com.example.dynamicjobmanagement.view.PersonCenter.NextStep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.User
import com.example.dynamicjobmanagement.view.Adapter.PersonCenter.SwitchAccountAdapter
import com.example.dynamicjobmanagement.view.MainActivity
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.example.dynamicjobmanagement.viewmodel.ViewModel.PersonCenterViewModel.SwitchAccountViewModel

class SwitchAccountActivity : AppCompatActivity(), SwitchAccountAdapter.OnAccountClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SwitchAccountAdapter
    private lateinit var viewModel: SwitchAccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_switch_account)
        // 初始化ViewModel
        viewModel = ViewModelProvider(this).get(SwitchAccountViewModel::class.java)

        // 初始化RecyclerView
        recyclerView = findViewById(R.id.switchAccount_users_RecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // 初始化Adapter
        adapter = SwitchAccountAdapter(this)
        recyclerView.adapter = adapter
        adapter.setData(UserRepository.getUsers())

        // 观察ViewModel中的数据变化
        viewModel.switchResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                // 跳转到主功能 Activity 并清除所有其它 Activity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                // 更新Adapter中的数据
                Toast.makeText(this, "已成功切换至用户：$info", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<ImageView>(R.id.switchAccount_back_ImageView).setOnClickListener {
            finish()
        }
    }

    override fun onAccountClick(user: User) {
        viewModel.onSwitchAccountClicked(user)
    }
}