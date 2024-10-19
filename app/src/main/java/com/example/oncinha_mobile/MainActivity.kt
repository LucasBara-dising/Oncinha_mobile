package com.example.oncinha_mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnJogar: ImageView
    private lateinit var btn_loja: ImageView

    private lateinit var textView_saldo: TextView

    var user =""
    var saldo = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intentLogin = intent
        user = intentLogin.getStringExtra("nome").toString()
        saldo = intentLogin.getIntExtra("saldo",0)


        textView_saldo= findViewById(R.id.textView_saldo)
        textView_saldo.text = "$ $saldo"

        Toast.makeText(this, "Bom dia $user", Toast.LENGTH_SHORT).show()

        btnJogar = findViewById(R.id.image_btn_jogar)
        btnJogar.setOnClickListener {
            abreRoleta()
        }

        btn_loja = findViewById(R.id.image_btn_loja)
        btn_loja.setOnClickListener {
            abreLoja()
        }
    }

    private fun abreRoleta(){
        val intent = Intent(this, Roleta::class.java)
        intent.putExtra("nome", user)
        intent.putExtra("saldo", saldo)
        startActivity(intent)
    }

    private fun abreLoja(){
        val intent = Intent(this, mercado::class.java)
        startActivity(intent)
    }
}