package com.example.dynamicjobmanagement.view.PersonCenter.NextStep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.model.model.User
import com.example.dynamicjobmanagement.view.Adapter.PersonCenter.SwitchAccountAdapter
import com.example.dynamicjobmanagement.view.MainActivity
import com.example.dynamicjobmanagement.view.login.LoginActivity
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository
import com.example.dynamicjobmanagement.viewmodel.viewModel.PersonCenterViewModel.SwitchAccountViewModel

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

        // 观察ViewModel中的数据变化
        viewModel.deleteResult.observe(this, Observer { result ->
            result.onSuccess {info ->
                if (UserRepository.getUsers().find { it.account==UserRepository.getUser().account }==null){
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }else{
                    adapter.setData(UserRepository.getUsers())
                }
                Toast.makeText(this, "已删除用户:${info}", Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })

        findViewById<ImageView>(R.id.switchAccount_back_ImageView).setOnClickListener {
            finish()
        }



        findViewById<Button>(R.id.switchAccount_addAccount_Button).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onAccountClick(user: User) {
        viewModel.onSwitchAccountClicked(user)
    }

    override fun onDeleteClick(user: User) {
        showDeleteJobConfirmDialog(user)
    }

    fun showDeleteJobConfirmDialog(user: User){
        var msg="确定删除用户"
        if (user.account==UserRepository.getUser().account){
            msg="确定删除当前登录用户"
        }

        AlertDialog.Builder(this).apply {
            setTitle("删除用户登录信息")
            setMessage("${msg} ${user.name} ？")
            setPositiveButton("是") { dialog, _ ->
                viewModel.deleteAccount(user.account)
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
}