package com.example.nequi_proyecto_componentes

import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MapaPuntosRecarga : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.mapa_puntos_recarga)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val cuenta = intent.getStringExtra("numeroDeCuenta")
        val mapa = findViewById<WebView>(R.id.WVMapa)
        val btnAtras = findViewById<ImageButton>(R.id.btnVolverMapa)
        val webSettings: WebSettings = mapa.settings
        webSettings.javaScriptEnabled = true
        mapa.loadUrl("https://www.google.com/maps/place/Cl.+68a+Sur+%2318z-10,+Bogotá/@4.5581537,-74.1489103,3a,75y,79.72h,70.17t/data=!3m7!1e1!3m5!1sNBc9eofs0EElGRtYbvlPeg!2e0!6shttps:%2F%2Fstreetviewpixels-pa.googleapis.com%2Fv1%2Fthumbnail%3Fcb_client%3Dmaps_sv.tactile%26w%3D900%26h%3D600%26pitch%3D19.82950617664632%26panoid%3DNBc9eofs0EElGRtYbvlPeg%26yaw%3D79.71964466126639!7i16384!8i8192!4m13!1m7!3m6!1s0x8e3f9f67e806fb91:0xf7f8d725e5b9312b!2sCl.+68a+Sur+%2318z-10,+Bogotá!3b1!8m2!3d4.5580431!4d-74.1488191!3m4!1s0x8e3f9f67e806fb91:0xf7f8d725e5b9312b!8m2!3d4.5580431!4d-74.1488191?coh=205410&entry=ttu&g_ep=EgoyMDI0MDgyOC4wIKXMDSoASAFQAw%3D%3D")

        btnAtras.setOnClickListener{
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }
    }


    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }

}