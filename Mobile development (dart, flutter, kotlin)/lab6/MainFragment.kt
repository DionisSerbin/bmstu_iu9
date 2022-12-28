package com.example.lab14

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.coroutines.delay

class RecyclerFragment : Fragment() {
    interface IListener {
        fun showNumber(value: Int, color: Int)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val button = view.findViewById<Button>(R.id.btn_submit)
        val login = view.findViewById<EditText>(R.id.et_user_name)
        val password = view.findViewById<EditText>(R.id.et_password)
        val cont = button.context
        button.setOnClickListener {
            val intent = Intent(cont, SecondActivity::class.java)
            intent.putExtra("Login", login.text.toString())
            intent.putExtra("Password", password.text.toString())
            cont.startActivity(intent)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

    }

    companion object {
        fun newInstance() = RecyclerFragment()
    }
}
