package com.example.oncinha_mobile

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class Roleta : AppCompatActivity() {
    private lateinit var slotsCol1: Array<ImageView>
    private lateinit var slotsCol2: Array<ImageView>
    private lateinit var slotsCol3: Array<ImageView>
    private lateinit var girarButton: ImageButton

    private lateinit var btnHome: ImageView

    private lateinit var infofatecCoin: TextView
    private lateinit var addCoins: TextView

    private lateinit var infoGanhos: TextView

    private lateinit var valorAposta: TextView
    private lateinit var addAposta: TextView
    private lateinit var subAposta: TextView
    private lateinit var resultadoJogo: TextView

    private var fatecCoin: Int=1000
    private var aposta: Int = 10
    private var ganhos: Int = 0
    private var user=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_roleta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultadoJogo = findViewById(R.id.textViewResultadoJogo)

        //valor da aposta
        valorAposta = findViewById(R.id.valorAposta)
        aposta = valorAposta.text.toString().toInt()

        val intentMain = intent
        //get user
        user = intentMain.getStringExtra("nome").toString()

        //valor fatec coins
        fatecCoin = intentMain.getIntExtra("saldo",0)
        Log.d("saldo", fatecCoin.toString())

        infofatecCoin = findViewById(R.id.InfoCoins)
        infofatecCoin.text = fatecCoin.toString()

        fatecCoin = infofatecCoin.text.toString().toInt()

        //ganhos
        infoGanhos = findViewById(R.id.InfoGanhos)
        ganhos = infoGanhos.text.toString().toInt()

        slotsCol1 = arrayOf(
            findViewById(R.id.slot1),
            findViewById(R.id.slot2),
            findViewById(R.id.slot3)
        )

        slotsCol2 = arrayOf(
            findViewById(R.id.slot4),
            findViewById(R.id.slot5),
            findViewById(R.id.slot6)
        )

        slotsCol3 = arrayOf(
            findViewById(R.id.slot7),
            findViewById(R.id.slot8),
            findViewById(R.id.slot9)
        )

        //roda roleta
        girarButton = findViewById(R.id.girar_button)
        girarButton.setOnClickListener {
            if (aposta <= (fatecCoin-aposta)){
                girarSlots()
            }
            else{
                Toast.makeText(this, "Saldo Insuficeinte", Toast.LENGTH_SHORT).show()
            }
        }

        //mais moedas
        addCoins = findViewById(R.id.addCoins)
        addCoins.setOnClickListener {
            //abre tela de loja
            val intent = Intent(this, Mercado::class.java)
            intent.putExtra("nome", user)
            intent.putExtra("saldo", fatecCoin)
            startActivity(intent)
        }

        //home
        btnHome = findViewById(R.id.imageViewHome)
        btnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("nome", user)
            intent.putExtra("saldo", fatecCoin)
            startActivity(intent)
        }

        //aumenta aposta
        addAposta = findViewById(R.id.addAposta)
        addAposta.setOnClickListener {
            //abre tela de loja
            if ((aposta+10)<fatecCoin){
                aposta += 10
                valorAposta.text = aposta.toString()
            }else{
                Toast.makeText(this, "Diminue a aposta", Toast.LENGTH_SHORT).show()
            }
        }

        //diminui aposta
        subAposta = findViewById(R.id.subAposta)
        subAposta.setOnClickListener {
            //abre tela de loja
            if ((aposta-10)>0){
                aposta -= 10
                valorAposta.text = aposta.toString()
            }else{
                aposta=0
            }
        }

    }

    @SuppressLint("SuspiciousIndentation")
    private fun girarSlots() {
        girarButton.isEnabled = false // Desabilita o botão durante a animação

        val url = "https://jungleoffortune.westus2.cloudapp.azure.com/v5/apimobile/roleta.php"  // Substitua pela URL do seu servidor

        // Cria o objeto JSON para enviar
        val params = HashMap<String, String>()
        params["usuario"] = user  // Substitua com o nome real do usuário
        params["aposta"] = aposta.toString()
        val jsonObject = JSONObject(params as Map<*, *>)


            // Fazendo a requisição com Volley
            val requestQueue = Volley.newRequestQueue(this)
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonObject,
                { response ->
                    try {
                        // Lendo a resposta JSON
                        val status = response.getString("status")
                        if (status == "success") {
                            val novoSaldo = response.getInt("saldo")
                            val resultado = response.getJSONArray("resultado")
                            val message = response.getString("message")

                            // Log para debug
                            Log.d("corpo", response.toString())

                            // Atualiza o saldo do usuário
                            fatecCoin = novoSaldo
                            infofatecCoin.text = novoSaldo.toString()

                            // Exibe a mensagem
                            resultadoJogo.text= message

                            // Atualiza os slots e animada
                            atualizarSlots(resultado)
                        } else {
                            val errorMessage = response.getString("message")
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Erro no processamento da resposta", Toast.LENGTH_SHORT).show()
                    }
                },
                {
                    Toast.makeText(this, "Erro na conexão com o servidor", Toast.LENGTH_SHORT).show()
                }
            )
            // Adiciona a requisição à fila
           requestQueue.add(jsonObjectRequest)

        girarButton.isEnabled = true // Habilita o botão novamente
    }

    private fun atualizarSlots(resultado: JSONArray) {
        //Anima Slots

        slotsCol1.forEach { slot ->
            // Animação de movimento para cima e para baixo
            val move = ValueAnimator.ofFloat(0f, 1500f, 0f)
            move.duration = 600
            move.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                slot.translationY = value
            }

            // Cria um AnimatorSet e inicia as animações
            val set = AnimatorSet()
            set.playTogether(move)
            set.start()
        }
        slotsCol2.forEach { slot ->
            // Animação de movimento para cima e para baixo
            val move = ValueAnimator.ofFloat(0f, 2000f, 0f)
            move.duration = 800
            move.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                slot.translationY = value
            }

            // Cria um AnimatorSet e inicia as animações
            val set = AnimatorSet()
            set.playTogether(move)
            set.start()
        }

        slotsCol3.forEach { slot ->
            // Animação de movimento para cima e para baixo
            val move = ValueAnimator.ofFloat(0f, 2500f, 0f)
            move.duration = 1000
            move.addUpdateListener { animation ->
                val value = animation.animatedValue as Float
                slot.translationY = value
            }

            // Cria um AnimatorSet e inicia as animações
            val set = AnimatorSet()
            set.playTogether(move)
            set.start()
        }

        for (i in 0 until resultado.length()) {
            val coluna = resultado.getJSONArray(i)
            Log.d("coluna", coluna.toString())
            slotsCol1[i].setImageResource(getSymbolResource(coluna.getString(0)))
            slotsCol2[i].setImageResource(getSymbolResource(coluna.getString(1)))
            slotsCol3[i].setImageResource(getSymbolResource(coluna.getString(2)))
        }
    }

    private fun getSymbolResource(resultado: String): Int {
        // Substitua com suas imagens
        return when (resultado) {
            "Boto" -> R.drawable.icon_boto
            "Onça" -> R.drawable.icon_onca
            "Arara" -> R.drawable.icon_arara
            "Macaco" -> R.drawable.icon_macaco
            "Capivara" -> R.drawable.icon_capivara
            "Moedas" -> R.drawable.icon_moedas
            "Espinho" -> R.drawable.icon_corpo_espinho
            "Tucano" -> R.drawable.icon_tucano
            "Tesouro" -> R.drawable.icon_tesouro
            else -> R.drawable.icon_capivara  // Um ícone padrão
        }
    }
}