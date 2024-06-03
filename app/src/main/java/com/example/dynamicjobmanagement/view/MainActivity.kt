package com.example.dynamicjobmanagement.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.view.CourseCenter.CourseCenterFragment
import com.example.dynamicjobmanagement.view.HopeSqure.HopeSquareFragment
import com.example.dynamicjobmanagement.view.PersonCenter.PersonCenterFragment
import com.example.dynamicjobmanagement.viewmodel.ViewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var courseCenterFragment: CourseCenterFragment
    private lateinit var hopeSquareFragment: HopeSquareFragment
    private lateinit var personCenterFragment: PersonCenterFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        saveFragment()

        val bottomNavView: BottomNavigationView = findViewById(R.id.mainActivity_BottomNavigationView)
        bottomNavView.setOnItemSelectedListener { menuItem ->
            viewModel.selectMenuItem(menuItem.itemId)
            true // 表示已处理点击事件
        }

        viewModel.selectedMenuItem.observe(this, Observer { menuItemId ->
            when (menuItemId) {
                R.id.main_menu_item_CourseCenter -> showFragment(courseCenterFragment)
                R.id.main_menu_item_HopeSquare -> showFragment(hopeSquareFragment)
                R.id.main_menu_item_PersonCenter -> showFragment(personCenterFragment)
                // 根据需要添加更多菜单项的处理
            }
        })

    }

    //保存fragment
    fun saveFragment(){
        courseCenterFragment=CourseCenterFragment.newInstance()
        hopeSquareFragment=HopeSquareFragment.newInstance()
        personCenterFragment=PersonCenterFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.mainActivity_FragmentContainerView, courseCenterFragment, "Fragment1")
//            .hide(courseCenterFragment)
            .add(R.id.mainActivity_FragmentContainerView, hopeSquareFragment, "Fragment2")
            .hide(hopeSquareFragment)
            .add(R.id.mainActivity_FragmentContainerView, personCenterFragment, "Fragment3")
            .hide(personCenterFragment)
            .commit()
    }

    //切换fragment
    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .hide(courseCenterFragment)
            .hide(hopeSquareFragment)
            .hide(personCenterFragment)
            .show(fragment)
            .commit()
    }
}