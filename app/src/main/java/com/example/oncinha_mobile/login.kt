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
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

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

        // URL do servidor onde está o login.php
        val url = "http://104.41.57.11/apimobile/login.php"

        // Criando o objeto JSON com os dados de login
        val jsonBody = JSONObject()
        jsonBody.put("usuario", user)
        jsonBody.put("senha", senha)

        Log.d("user", user.toString())

        //Toast.makeText(this, "User $user", Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "Senha $senha", Toast.LENGTH_SHORT).show()


        btnLogin = findViewById(R.id.btnLogin)
        btnLogin.setOnClickListener {
            // Criando uma nova fila de requisição com o Volley
            val requestQueue = Volley.newRequestQueue(this)

            // Requisição POST usando Volley
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                { response ->
                    try {
                        // Log para mostrar o JSON completo
                        println("Resposta JSON completa: $response")

                        // Verificar o conteúdo do JSON recebido
                        val status = response.getString("status")
                        if (status == "success") {
                            // Usando optString para evitar crashes
                            val nome = response.optString("nome", "Nome não disponível")
                            val saldo = response.optString("saldo", "0")

                            // Log para debug
                            println("Nome: $nome, Saldo: $saldo")

                            // Redireciona para a DashboardActivity
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("nome", nome)
                            intent.putExtra("saldo", saldo)
                            startActivity(intent)
                            finish() // Finaliza a MainActivity
                        } else {
                            val message = response.getString("message")
                            Toast.makeText(this, "Erro: $message", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        // Captura qualquer erro durante o processamento da resposta JSON
                                e.printStackTrace()
                        Toast.makeText(this, "Erro ao processar a resposta: ${e.message}", Toast.LENGTH_SHORT).show()
                        println("${e.message}")
                    }
                },
                { error ->
                    // Erro na requisição
                    error.printStackTrace()
                    Toast.makeText(this, "Erro de conexão: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            )

            // Adicionando a requisição à fila
            requestQueue.add(jsonObjectRequest)
        }


    }

    private fun login(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza a MainActivity
    }
}





