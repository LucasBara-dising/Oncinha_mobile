package com.example.oncinha_mobile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class mercado : AppCompatActivity() {
    var fatecCoin: Int=0
    var user=""

    private lateinit var InfofatecCoin: TextView

    private lateinit var imageViewHome: ImageView

    private lateinit var add10Moedas: CardView
    private lateinit var add100Moedas: CardView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mercado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intentMain = intent
        //get user
        user = intentMain.getStringExtra("nome").toString()

        //valor fatec coins
        fatecCoin = intentMain.getIntExtra("saldo",0)
        InfofatecCoin = findViewById(R.id.textViewSaldo)
        InfofatecCoin.text = fatecCoin.toString()

        //Add mais 10 moedas
        add10Moedas = findViewById(R.id.add10Moedas)
        add10Moedas.setOnClickListener {
            fatecCoin += 10
            InfofatecCoin.text = fatecCoin.toString()
        }

        //Add mais 100 moedas
        add100Moedas = findViewById(R.id.add100Moedas)
        add100Moedas.setOnClickListener {
            fatecCoin += 100
            InfofatecCoin.text = fatecCoin.toString()
        }


        //Volta home
        imageViewHome = findViewById(R.id.imageViewHome)
        imageViewHome.setOnClickListener {
            abreHome()
        }

    }

    fun abreHome(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nome", user)
        intent.putExtra("saldo", fatecCoin)
        startActivity(intent)
    }
}

//loja.php