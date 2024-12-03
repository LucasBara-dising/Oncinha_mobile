package com.example.oncinha_mobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class Mercado : AppCompatActivity() {
    private var fatecCoin: Int=0
    private var user=""

    private lateinit var infofatecCoin: TextView

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
        //user = "luchas"
        //fatecCoin = 100

        //valor fatec coins
        fatecCoin = intentMain.getIntExtra("saldo",0)
        infofatecCoin = findViewById(R.id.textViewSaldo)
        infofatecCoin.text = "$ $fatecCoin"

        mostraItens()

        //Add mais 10 moedas
        add10Moedas = findViewById(R.id.add10Moedas)
        add10Moedas.setOnClickListener {
            fatecCoin += 10
            infofatecCoin.text = fatecCoin.toString()
        }

        //Add mais 100 moedas
        add100Moedas = findViewById(R.id.add100Moedas)
        add100Moedas.setOnClickListener {
            fatecCoin += 100
            infofatecCoin.text = fatecCoin.toString()
        }


        //Volta home
        imageViewHome = findViewById(R.id.imageViewHome)
        imageViewHome.setOnClickListener {
            abreHome()
        }

        //comrpa(35, 1)

    }

    private fun abreHome(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nome", user)
        intent.putExtra("saldo", fatecCoin)
        startActivity(intent)
    }

    private fun mostraItens() {

        val url = "https://jungleoffortune.westus2.cloudapp.azure.com/v5/apimobile/loja.php"  // Substitua pela URL do seu servidor

        // Fazendo a requisição com Volley
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    println(response)
                    // Lendo a resposta JSON
                    val status = response.getString("status")
                    if (status == "success") {
                        // Log para debug
                        val itensJson = response.getJSONArray("itens")
                        val itens = itensJson.toString()

                        Log.d("corpo", itens)

                        // Cria uma instância do Gson
                        val gson = Gson()

                        // Converte o JSON para uma lista de objetos Item
                        val itemType = object : TypeToken<ArrayList<Item>>() {}.type
                        val itemList: ArrayList<Item> = gson.fromJson(itens, itemType)

                        val itemBadges = itemList.filter  { it.tipo == "badge" }
                        val itemAvatar = itemList.filter  { it.tipo == "avatar" }

                        //controi list
                        val recyclerViewBadges: RecyclerView = findViewById(R.id.recyclerView)
                        recyclerViewBadges.layoutManager = GridLayoutManager(this, 2) // 2 colunas

                        val adapterBadge = ItemAdapter(itemBadges)
                        recyclerViewBadges.adapter = adapterBadge

                        // Applying OnClickListener to our Adapter
                        adapterBadge.setOnClickListener(object :
                            ItemAdapter.OnClickListener {
                            override fun onClick(position: Int, model: Item) {
                                Toast.makeText(this@Mercado, "Clicked on  ${model.nome}", Toast.LENGTH_LONG).show()
                                Log.d("Item", "${model}")

                            }
                        })

                        //controi list
                        val recyclerViewAvatar: RecyclerView = findViewById(R.id.recyclerViewAvatar)
                        recyclerViewAvatar.layoutManager = GridLayoutManager(this, 2) // 2 colunas

                        val adapterAvatar = ItemAdapter(itemAvatar)
                        recyclerViewAvatar.adapter = adapterAvatar

                        // Applying OnClickListener to our Adapter
                        adapterBadge.setOnClickListener(object :
                            ItemAdapter.OnClickListener {
                            override fun onClick(position: Int, model: Item) {
                                Toast.makeText(this@Mercado, "Clicked on  ${model.nome}", Toast.LENGTH_LONG).show()
                                Log.d("Item", "${model}")

                            }
                        })

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

    }
    fun comrpa(user:Int, item:Int){

        val jsonBody = JSONObject()
        jsonBody.put("user_id", user)
        jsonBody.put("item_id", item)

        val url = "https://jungleoffortune.westus2.cloudapp.azure.com/v5/apimobile/loja.php"  // Substitua pela URL do seu servidor


        // Fazendo a requisição com Volley
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                try {

                    println(response)
                    // Lendo a resposta JSON
                    val status = response.getString("status")
                    if (status == "success") {
                        // Log para debug

                        Log.d("corpo", "Oiii")

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

    }
}

//loja.php