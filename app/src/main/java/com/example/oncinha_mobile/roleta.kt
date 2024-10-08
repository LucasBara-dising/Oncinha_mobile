package com.example.oncinha_mobile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.w3c.dom.Text
import java.util.Arrays
import java.util.Random

class roleta : AppCompatActivity() {
    private lateinit var slots_col1: Array<ImageView>
    private lateinit var slots_col2: Array<ImageView>
    private lateinit var slots_col3: Array<ImageView>
    private lateinit var girarButton: Button

    private lateinit var InfofatecCoin: TextView
    private lateinit var TextAposta: TextView

    private val random = Random()

    var fatecCoin: Int=0
    var aposta: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_roleta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        TextAposta = findViewById(R.id.valorAposta)
        aposta = TextAposta.text.toString().toInt()

        InfofatecCoin = findViewById(R.id.InfoCoins)
        fatecCoin = InfofatecCoin.text.toString().toInt()


        slots_col1 = arrayOf(
            findViewById(R.id.slot1),
            findViewById(R.id.slot2),
            findViewById(R.id.slot3)
        )

        slots_col2 = arrayOf(
            findViewById(R.id.slot4),
            findViewById(R.id.slot5),
            findViewById(R.id.slot6)
        )

        slots_col3 = arrayOf(
            findViewById(R.id.slot7),
            findViewById(R.id.slot8),
            findViewById(R.id.slot9)
        )

        girarButton = findViewById(R.id.girar_button)

        girarButton.setOnClickListener {
            if (aposta < (fatecCoin-aposta)){
                girarSlots()
            }
            else{
                Toast.makeText(this, "Saldo Insuficeinte", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun girarSlots() {
        girarButton.isEnabled = false // Desabilita o botão durante a animação

        slots_col1.forEach { slot ->
            // Animação de rotação
            val rotation = ObjectAnimator.ofFloat(slot, "rotation", 0f, 1080f)
            rotation.duration = 1100

            // Animação de movimento para cima e para baixo
            val move = ValueAnimator.ofFloat(0f, 1000f, 0f)
            move.duration = 1100
            move.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                slot.translationY = value
            }

            // Cria um AnimatorSet e inicia as animações
            val set = AnimatorSet()
            set.playTogether(rotation, move)
            set.start()
        }

        slots_col2.forEach { slot ->
            // Animação de rotação
            val rotation = ObjectAnimator.ofFloat(slot, "rotation", 0f, 1080f)
            rotation.duration = 1100

            // Animação de movimento para cima e para baixo
            val move = ValueAnimator.ofFloat(0f, 1500f, 0f)
            move.duration = 1100
            move.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                slot.translationY = value
            }

            // Cria um AnimatorSet e inicia as animações
            val set = AnimatorSet()
            set.playTogether(rotation, move)
            set.start()
        }

        slots_col3.forEach { slot ->
            // Animação de rotação
            val rotation = ObjectAnimator.ofFloat(slot, "rotation", 0f, 1080f)
            rotation.duration = 1100

            // Animação de movimento para cima e para baixo
            val move = ValueAnimator.ofFloat(0f, 2000f, 0f)
            move.duration = 1100
            move.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                slot.translationY = value
            }

            // Cria um AnimatorSet e inicia as animações
            val set = AnimatorSet()
            set.playTogether(rotation, move)
            set.start()
        }

        // Aguarda 3 segundos para mostrar o resultado
        Handler().postDelayed({
            mostrarResultado()
        }, 800)
    }

    private fun mostrarResultado() {
        // Gera números aleatórios para os símbolos
        val resultados1 = IntArray(slots_col1.size) { random.nextInt(9) } // Exemplo: 6 símbolos possíveis
        val resultados2 = IntArray(slots_col2.size) { random.nextInt(9) } // Exemplo: 6 símbolos possíveis
        val resultados3 = IntArray(slots_col3.size) { random.nextInt(9) } // Exemplo: 6 símbolos possíveis

        // Define os símbolos de acordo com os resultados
        for (i in slots_col1.indices) {
            // Define a imagem do slot com base no resultado (resultados[i])
            slots_col1[i].setImageResource(getSymbolResource(resultados1[i]))
        }

        for (i in slots_col2.indices) {
            // Define a imagem do slot com base no resultado (resultados[i])
            slots_col2[i].setImageResource(getSymbolResource(resultados2[i]))
        }

        for (i in slots_col3.indices) {
            // Define a imagem do slot com base no resultado (resultados[i])
            slots_col3[i].setImageResource(getSymbolResource(resultados3[i]))
        }


        // Verifica se ganhou ou perdel
        if ( //Horizontal
            resultados1[0] == resultados1[1] && resultados1[0] == resultados1[2] ||
            resultados2[0] == resultados2[1] && resultados2[0] == resultados2[2] ||
            resultados3[0] == resultados3[1] && resultados3[0] == resultados3[2]) {
            aposta+=10
        }

        if (//Cruzado
            resultados1[0] == resultados2[1] && resultados1[0] == resultados3[2] ||
            resultados1[2] == resultados2[1] && resultados1[2] == resultados3[0]){
            aposta+=20
        }

        if (//Vertical
            resultados1[0] == resultados2[0] && resultados1[0] == resultados3[0] ||
            resultados1[1] == resultados2[1] && resultados1[1] == resultados3[1] ||
            resultados1[2] == resultados2[2] && resultados1[2] == resultados3[2]){
            aposta+=10
        }

        if (aposta==0){
            Toast.makeText(this, "Parabéns! Você ganhou! $aposta", Toast.LENGTH_SHORT).show()
        }
        else {
            Toast.makeText(this, "Que pena! Tente novamente!", Toast.LENGTH_SHORT).show()
        }

        girarButton.isEnabled = true // Habilita o botão novamente
    }

    private fun getSymbolResource(resultado: Int): Int {
        // Substitua com suas imagens
        return when (resultado) {
            0 -> R.drawable.icon_boto
            1 -> R.drawable.icon_onca
            2 -> R.drawable.icon_arara
            3 ->  R.drawable.icon_macaco
            4 ->  R.drawable.icon_capivara
            5 ->  R.drawable.icon_moedas
            6 ->  R.drawable.icon_corpo_espinho
            7 ->  R.drawable.icon_tucano
            8 ->  R.drawable.icon_tesouro
            else -> R.drawable.icon_capivara
        }
    }
}