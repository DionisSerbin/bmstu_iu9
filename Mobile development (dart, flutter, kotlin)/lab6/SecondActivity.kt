package com.example.lab14

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.log


class SecondActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.second_activity)
        val login = intent.extras?.getString("Login")
        val password = intent.extras?.getString("Password")
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.second_activity, SecondFragment.newInstance(login, password),
                    "info")
                .commit()
        }
    }
}
