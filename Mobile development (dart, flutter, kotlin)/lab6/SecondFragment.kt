package com.example.lab14

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment



class SecondFragment : Fragment(R.layout.second_fragment) {
    private var login: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            login = it.getString(LOGIN)
            password = it.getString(PASSWORD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.second_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.btn_submit_second)
        val editText = view.findViewById<EditText>(R.id.request_string)
        button.setOnClickListener{
            Toast.makeText(
                button.context,
                editText.text.toString() + "\n" + login + "\n" + password,
                Toast.LENGTH_LONG).show()
        }

    }

    companion object {
        private const val LOGIN = "Login"
        private const val PASSWORD = "Password"
        @JvmStatic
        fun newInstance(login: String?, password: String?) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(LOGIN, login)
                    putString(PASSWORD, password)
                }
            }
    }
}
