package com.example.lab15

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mail = intent.extras?.getString("mail")
        val password = intent.extras?.getString("password")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(
                    R.id.main_activity,
                    MainFragment.newInstance(mail, password),
                    "category"
                )
                .commit()
        }
    }
}