package com.example.dynamicjobmanagement.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.dynamicjobmanagement.R
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.dynamicjobmanagement.databinding.ActivityLoginBinding
import com.example.dynamicjobmanagement.viewmodel.UserRepository
import com.example.dynamicjobmanagement.viewmodel.LoginViewModel
import com.example.dynamicjobmanagement.viewmodel.LoginViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory(UserRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel!!.loginResult.observe(this, Observer { result ->
            result.onSuccess { info ->
                Toast.makeText(this, "Welcome, $info", Toast.LENGTH_SHORT).show()
            }.onFailure { exception ->
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
