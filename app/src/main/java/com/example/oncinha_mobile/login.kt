package com.example.oncinha_mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class login : AppCompatActivity() {

    private lateinit var inputUser : EditText
    private lateinit var inputSenha : EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        inputUser = findViewById(R.id.inputUser)
        inputSenha = findViewById(R.id.inputSenha)

        val user = inputUser.text
        val senha = inputSenha.text

        Log.d("user", user.toString())

        //Toast.makeText(this, "User $user", Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "Senha $senha", Toast.LENGTH_SHORT).show()

        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            login()
        }

    }

    private fun login(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}