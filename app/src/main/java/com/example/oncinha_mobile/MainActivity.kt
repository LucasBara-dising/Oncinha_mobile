package com.example.oncinha_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnJogar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = intent
        val user = intent.getStringExtra("nome")

        println("Bom dia $user")
        Toast.makeText(this, "Bom dia $user", Toast.LENGTH_SHORT).show()

        btnJogar = findViewById(R.id.image_btn_jogar)
        btnJogar.setOnClickListener {
            abreRoleta()
        }
    }

    private fun abreRoleta(){
        val intent = Intent(this, roleta::class.java)
        startActivity(intent)
    }
}