package com.example.dynamicjobmanagement.viewmodel.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dynamicjobmanagement.R

class MainViewModel: ViewModel() {
    val selectedMenuItem = MutableLiveData<Int>()

    init {
        // 初始化选中的菜单项
        selectedMenuItem.value = R.id.main_menu_item_CourseCenter // 默认选中第一个菜单项
    }

    fun selectMenuItem(menuItemId: Int) {
        selectedMenuItem.value = menuItemId
    }
}