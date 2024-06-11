package com.example.dynamicjobmanagement.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.databinding.ActivityLoginBinding
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.view.MainActivity
import com.example.dynamicjobmanagement.viewmodel.viewModel.loginViewModel.LoginViewModel
import com.example.dynamicjobmanagement.viewmodel.Factory.Login.LoginViewModelFactory
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.loginResult.observe(this, Observer { result ->
            result.onSuccess { info ->
                Toast.makeText(this, "欢迎登录, $info", Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
                println("error:${exception.message}")
            }
        })

        viewModel.navigateToMain.observe(this, Observer {
            if(UserRepository.getUserType()==UserType.STUDENT){
                // 当导航事件发生时，启动 MainActivity_student
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()

                startActivity(intent)
            }else{
                // 当导航事件发生时，启动 MainActivity_teacher
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
             
            }
        })

        findViewById<TextView>(R.id.login_userAgreement_TextView).setOnClickListener {
            showAgreementAlertDialog()
        }
    }

    private fun showAgreementAlertDialog() {
        val instructions=R.string.agreement_content
        // 创建AlertDialog
        AlertDialog.Builder(this)
            .setTitle("用户协议")
            .setMessage(instructions)
            .setPositiveButton("已阅读") { dialog, _ ->
                // 用户点击同意按钮的处理逻辑，可以根据需求添加具体操作
                dialog.dismiss()
            }
            .show()
    }
}
