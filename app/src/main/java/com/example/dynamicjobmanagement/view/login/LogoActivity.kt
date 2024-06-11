package com.example.dynamicjobmanagement.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository

class LogoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        UserRepository.init(this)
        // 设置定时跳转到主页面
        Handler(Looper.getMainLooper()).postDelayed({
            // 启动LoginActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            // 结束LogoActivity
            finish()
        }, 2000) // 延迟2秒

    }
}