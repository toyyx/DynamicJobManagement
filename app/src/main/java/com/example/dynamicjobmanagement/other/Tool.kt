package com.example.dynamicjobmanagement.other

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
//test
class Tool: Application() {
    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context//全局通用上下文
    }
    override fun onCreate() {
        super.onCreate()
        context =applicationContext
    }
}