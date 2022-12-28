package com.example.lab15

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SignInActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.sign_in_activity, SignInFragment.newInstance())
                .commitNow()
        }
    }


}