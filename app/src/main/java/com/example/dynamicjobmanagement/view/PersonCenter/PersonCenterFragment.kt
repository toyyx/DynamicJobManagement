package com.example.dynamicjobmanagement.view.PersonCenter

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.view.PersonCenter.NextStep.PersonalInformationActivity
import com.example.dynamicjobmanagement.view.PersonCenter.NextStep.SettingsActivity
import com.example.dynamicjobmanagement.view.PersonCenter.NextStep.SwitchAccountActivity
import com.example.dynamicjobmanagement.viewmodel.Repository.UserRepository

/**
 * A simple [Fragment] subclass.
 * Use the [PersonCenterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PersonCenterFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_person_center, container, false)

        view.findViewById<TextView>(R.id.personCenter_userName_TextView).text=UserRepository.getUser().name

        view.findViewById<LinearLayout>(R.id.personCenter_toSwitchAccount_LinearLayout).setOnClickListener {
            val intent = Intent(context, SwitchAccountActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.personCenter_toPersonalInfo_TextView).setOnClickListener {
            val intent = Intent(context, PersonalInformationActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<LinearLayout>(R.id.personCenter_toSettings_TextView).setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        fun newInstance(): PersonCenterFragment {
            return PersonCenterFragment()
        }
    }
}