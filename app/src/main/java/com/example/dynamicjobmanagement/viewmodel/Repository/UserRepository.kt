package com.example.dynamicjobmanagement.viewmodel.Repository

import android.content.Context
import android.content.SharedPreferences
import com.example.dynamicjobmanagement.model.model.Student
import com.example.dynamicjobmanagement.model.model.Teacher
import com.example.dynamicjobmanagement.model.model.User
import com.example.dynamicjobmanagement.model.model.UserType
import com.example.dynamicjobmanagement.model.network.RetrofitClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object UserRepository {
    private var user: User? = null
    private var userType: UserType? = null
    private var user_Student: Student? = null
    private var user_Teacher: Teacher? = null
    private const val PREFS_NAME = "UserPrefs"
    private const val PREFS_KEY_USERS = "users"
    private lateinit var sharedPreferences: SharedPreferences

    // 初始化方法，用于传递Context
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }


    fun saveUser(user: User) {
        val userList = getUsers().toMutableList()
        // 检查是否已存在同名用户
        val userToAdd = userList.find { it.account == user.account }
        if(userToAdd!=null){//若重复则删除
            userList.remove(userToAdd)
        }
        userList.add(user)
        val json = Gson().toJson(userList)
        sharedPreferences.edit().putString(PREFS_KEY_USERS, json).apply()
    }

    fun deleteUser(account: String) {
        val userList = getUsers().toMutableList()
        val userToRemove = userList.find { it.account == account }
        if (userToRemove != null) {
            userList.remove(userToRemove)
            val json = Gson().toJson(userList)
            sharedPreferences.edit().putString(PREFS_KEY_USERS, json).apply()
        }
    }

    fun getUsers(): List<User> {
        val json = sharedPreferences.getString(PREFS_KEY_USERS, null)
        return if (json != null) {
            val type = object : TypeToken<List<User>>() {}.type
            Gson().fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun setUser(user:Student) {
        this.userType=UserType.STUDENT
        this.user = user
        this.user_Student=user
        saveUser(user)
    }

    fun setUser(user:Teacher) {
        this.userType=UserType.TEACHER
        this.user = user
        this.user_Teacher=user
        saveUser(user)
    }

    fun getUser():User{
        return user!!
    }

    fun getStudentUser():Student?{
        return user_Student
    }

    fun getTeacherUser():Teacher?{
        return user_Teacher
    }

    fun getUserType():UserType?{
        return userType
    }

    suspend fun login(account: String, password: String) = RetrofitClient.apiService.login(account, password)
}
