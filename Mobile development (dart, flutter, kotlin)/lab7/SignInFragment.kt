package com.example.lab15

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class SignInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val signUpButton = view.findViewById<Button>(R.id.sign_in_button)

        signUpButton.setOnClickListener {
            val email = view.findViewById<TextInputEditText>(R.id.email_text).text.toString()
            val pass = view.findViewById<TextInputEditText>(R.id.password_text).text.toString()


            if (email.isNotEmpty() && pass.isNotEmpty()) {
                val intent = Intent(signUpButton.context, MainActivity::class.java)
                intent.putExtra("mail", email)
                intent.putExtra("password", pass)
                startActivity(intent)
            } else {
                Toast.makeText(signUpButton.context, "Вы не ввели все данные", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SignInFragment()
    }
}