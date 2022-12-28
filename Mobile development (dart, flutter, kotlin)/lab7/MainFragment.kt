package com.example.lab15

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth


class MainFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    private var mail: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mail = it.getString(MAIL)
            password = it.getString(PASSWORD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        val signInReturn = view.findViewById<Button>(R.id.return_sign_in)

        signInReturn.setOnClickListener {
            val intent = Intent(signInReturn.context, SignInActivity::class.java)
            startActivity(intent)
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.progress_category)

        progressBar.isVisible = true

        progressBar.setOnClickListener(null)

        if (mail?.isNotEmpty() == true && password?.isNotEmpty() == true) {
            firebaseAuth.createUserWithEmailAndPassword(mail!!, password!!).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(context, "Регистрация успешна", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(context, "Что-то пошло не так, попробуйте снова", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    companion object {
        private const val MAIL = "mail"
        private const val PASSWORD = "password"

        @JvmStatic
        fun newInstance(param1: String?, param2: String?) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(MAIL, param1)
                    putString(PASSWORD, param2)
                }
            }
    }
}