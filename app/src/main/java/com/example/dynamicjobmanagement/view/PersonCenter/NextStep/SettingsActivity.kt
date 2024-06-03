package com.example.dynamicjobmanagement.view.PersonCenter.NextStep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.dynamicjobmanagement.R
import com.example.dynamicjobmanagement.view.Login.LoginActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        findViewById<ImageView>(R.id.settings_back_ImageView).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.settings_toSwitchAccount_Button).setOnClickListener {
            val intent = Intent(this, SwitchAccountActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.settings_Logout_Button).setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
}