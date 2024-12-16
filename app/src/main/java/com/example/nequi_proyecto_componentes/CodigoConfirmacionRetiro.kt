package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CodigoConfirmacionRetiro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.codigo_confirmacion_sacar_plata)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val codigo = findViewById<TextView>(R.id.txtCodigoRetiro)
        val volver = findViewById<ImageButton>(R.id.btnVolver_cod_con_retiro)
        val texto = intent.getStringExtra("mensaje2")
        val mensaje: TextView = findViewById(R.id.txtMensajeConfirmacionRetiro)
        val btnOjo = findViewById<ImageButton>(R.id.btnOjo_cod_con_retiro)
        val cuenta = intent.getStringExtra("numeroDeCuenta")
        mensaje.text= texto
        val numeroRandom = (100000..999999).random().toString()
        codigo.text = numeroRandom

        volver.setOnClickListener{
            val intent = Intent(this, PantallaDeCarga::class.java).apply{
                putExtra("proximaPagina" , "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }
        btnOjo.setOnClickListener{
            if(!codigo.text.toString().equals("********")){
                codigo.text = "********"
            }else{
                codigo.text = numeroRandom
            }
        }


    }

    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }

}