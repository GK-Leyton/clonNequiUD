package com.example.nequi_proyecto_componentes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide


class PantallaDeCarga : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.pantalla_de_carga)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val proxima = intent.getStringExtra("proximaPagina")
        val cuenta = intent.getStringExtra("numeroDeCuenta")
        //Toast.makeText(this,"Valor recivido en pantalla de carga" + cuenta, Toast.LENGTH_SHORT).show()
        val imagen = findViewById<ImageView>(R.id.gifCarga)
        Glide.with(this)
            .load(R.drawable.icono_carga)
            .into(imagen);

        //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
        //*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/*/
        if (proxima.equals("menu_principal")) {
            val ruta = MenuPrincipal::class.java
            Thread {
                val intent = Intent(this, ruta).apply{
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }
        if (proxima.equals("codigo_confirmacion_retiro_cajero") || proxima.equals("codigo_confirmacion_retiro_punto_fisico")) {
            val mensajePas = intent.getStringExtra("mensaje")
            val ruta = CodigoConfirmacionRetiro::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("mensaje2", mensajePas)
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("PedirPlataNequi") || proxima.equals("PedirPlataTransfiya")) {
            val mensajePas = intent.getStringExtra("mensaje")
            val numeroCuenta = intent.getStringExtra("numeroDeCuenta")
            val ruta = PedirPlata::class.java

            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("mensaje2", mensajePas)
                    putExtra("numeroDeCuenta" , numeroCuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("enviar_plata") || proxima.equals("enviar_plata_transfiya")) {
            val mensajePas = intent.getStringExtra("mensaje")
            val ruta = EnviarPlata::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("mensaje2", mensajePas)
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }


        if (proxima.equals("mapa")) {

            val ruta = MapaPuntosRecarga::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("codigo_regalo")) {

            val ruta = RecargarNequiCodigoRegalo::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("numeroDeCuenta" , cuenta)
                }

                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("acceder_bolsillo")) {

            val nombre_bolsillo = intent.getStringExtra("nombre_bolsillo")
            val cantidad_en_bolsillo = intent.getStringExtra("cantidad_en_bolsillo")
            val ruta = AccederBolsillo::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("nombre_bolsillo_2", nombre_bolsillo)
                    putExtra("cantidad_en_bolsillo_2", cantidad_en_bolsillo)
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }



        if (proxima.equals("acceder_colchon")) {


            val ruta = AccederColchon::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }


        if (proxima.equals("acceder_meta")) {

            val nombre_meta = intent.getStringExtra("nombre_meta")
            val cantidad_en_meta = intent.getStringExtra("cantidad_en_meta")
            val cantidad_final_meta = intent.getStringExtra("cantidad_final_meta")
            val fecha_inicio_meta = intent.getStringExtra("fecha_inicio_meta")
            val ruta = AccederMeta::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("nombre_meta_2", nombre_meta)
                    putExtra("cantidad_en_meta_2", cantidad_en_meta)
                    putExtra("cantidad_final_meta_2", cantidad_final_meta)
                    putExtra("fecha_inicio_meta_2", fecha_inicio_meta)
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }


        if (proxima.equals("ver_notificaciones")) {


            val ruta = AccederNotificaciones::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }



        if (proxima.equals("ver_perfil")) {


            val ruta = AccederPerfil::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("salir")) {
            val ruta = MainActivity::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                    putExtra("numeroDeCuenta" , cuenta)
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }

        if (proxima.equals("crear_usuario")) {
            val ruta = CreacionUsuario::class.java
            Thread {
                val intent = Intent(this, ruta).apply {
                }
                Thread.sleep(200)
                startActivity(intent)
            }.start()
        }
    }

    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }
}

