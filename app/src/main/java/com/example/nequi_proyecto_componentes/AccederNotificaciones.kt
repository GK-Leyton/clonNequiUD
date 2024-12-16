package com.example.nequi_proyecto_componentes

import PopupDetallesDelMovimientoMovimientos
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperCancel
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirm


class AccederNotificaciones : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.acceder_notificaciones)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // Encontrar el LinearLayout dentro del ScrollView
        val linearLayout = findViewById<LinearLayout>(R.id.linearLayout)
        val btnVolver = findViewById<ImageView>(R.id.btnAtras_Notificaciones)
        val cuenta = intent.getStringExtra("numeroDeCuenta")


        btnVolver.setOnClickListener {
            val intent = Intent(this, PantallaDeCarga::class.java).apply {
                putExtra("proximaPagina", "menu_principal")
                putExtra("numeroDeCuenta" , cuenta)
            }
            startActivity(intent)
        }

        // Agregar contenido dinámico
        addContentToScrollView(linearLayout , cuenta.toString())
    }



    private fun addContentToScrollView(linearLayout: LinearLayout , cuenta : String) {
        val inflater = LayoutInflater.from(this)
        val bdHelper = DatabaseHelper(this)
        val idProp = bdHelper.obtenerUsuarioIdPorCuentaId(cuenta).toString()
        val notificaciones = bdHelper.obtenerNotificacionesPorCuenta(cuenta , idProp)

        for(notify in notificaciones){
            // Infla el layout del item desde el archivo XML

            if(notify["Tipo"] == "repuesta Peticion"){
                val itemView = inflater.inflate(R.layout.item_layout_notificacion, linearLayout, false)

                // Configura los elementos del FrameLayout
                val icono = itemView.findViewById<ImageView>(R.id.iconoMegafono)
                val txtTitulo = itemView.findViewById<TextView>(R.id.txtTituloNotificacionItem)
                val txtDetalles = itemView.findViewById<TextView>(R.id.txtDetalleNotificacion)




                // Establece datos dinámicos en cada TextView
                txtTitulo.text = notify["Tipo"]?.toString() ?: "Tipo desconocido"
                txtDetalles.text = notify["Mensaje"]?.toString() ?: "Mensaje desconocido"

                // Establece el evento OnClick para cada item
                itemView.setOnClickListener {
                    // Cierra el PopupWindow actual antes de abrir uno nuevo
                    var mensaje = notify["Mensaje"]?.toString() ?: "Mensaje desconocido"
                    var nombreUsuario = notify["Nombre"]?.toString() ?: "Nombre desconocido"
                    val monto = notify["Monto"]?.toString() ?: "Monto desconocido"
                    val popupHelper = PopupHelperInformacionPeticion(this)
                    popupHelper.showPopup(itemView , mensaje , nombreUsuario , monto)
                }

                // Agrega el FrameLayout al LinearLayout del ScrollView
                linearLayout.addView(itemView)
            }else{
                val itemView = inflater.inflate(R.layout.item_layout_notificacion_2, linearLayout, false)

                // Configura los elementos del FrameLayout
                val icono = itemView.findViewById<ImageView>(R.id.iconoMegafono)
                val txtTitulo = itemView.findViewById<TextView>(R.id.txtTituloNotificacionItem2)
                val txtNombreusUarioPide = itemView.findViewById<TextView>(R.id.txtNombreusUarioPide)
                val txtCuantoPide = itemView.findViewById<TextView>(R.id.txtCuantoPide)
                val btnCancelar = itemView.findViewById<TextView>(R.id.btnCancelarNotificacionPedirPlataEntrante)
                val btnAceptar = itemView.findViewById<TextView>(R.id.btnAceptarNotificacionPedirPlataEntrante)

                if(notify["Estado"] == "2"){
                    btnCancelar.isEnabled = false
                    btnAceptar.isEnabled = false
                    btnCancelar.visibility = View.INVISIBLE
                    btnAceptar.visibility = View.INVISIBLE

                }


                var idUsuario = notify["CuentaEnviador"] ?: "ID desconocido"

                val nombreUsiarioPide = bdHelper.obtenerNombreUsuarioPorId(idUsuario)


                // Establece datos dinámicos en cada TextView
                txtTitulo.text = notify["Tipo"]?.toString() ?: "Tipo desconocido"
                txtNombreusUarioPide.text = nombreUsiarioPide.toString()
                txtCuantoPide.text = "$" + notify["Monto"]?.toString() ?: "Monto desconocido"


                btnCancelar.setOnClickListener {
                    //
                    //
                    //
                    //
                    //autoacceder a notificaciones con el cambio en base de datos para que se actualice la notificacion
                    //hacer que los botones se vuelvan del mismo color del item para que no se vean ademas de que se desactive el clickeo
                    //realizar el respectivo cambio en la base de datos
                    val idMensaje = notify["ID"]?.toString() ?: "ID desconocido"
                    val nuevoEstado = "2"


                    bdHelper.actualizarEstadoDeUNMensaje(idMensaje, nuevoEstado)
                    var cantidad = (notify["Monto"]?.toString() ?: "Monto desconocido")
                    var cantidad2 = cantidad.toInt()
                    val idExterno = notify["CuentaEnviador"]?.toString() ?: "ID desconocido"
                    val tipo = "repuesta Peticion"
                    val mensajeM = "Su peticion ha sido rechazada."
                    val fechaActual = java.time.LocalDate.now().toString()

                    val usuarioID = bdHelper.obtenerUsuarioIdPorCuentaId(cuenta).toString()
                    val usaurioExternoId = idExterno
                    val monto = cantidad2
                    val estado = "1"

                    bdHelper.insertarMensaje(tipo, mensajeM, fechaActual, usaurioExternoId, usuarioID, monto.toString() , estado)



                    val popupHelper = PopupHelperCancel(this)
                    popupHelper.showConfirmationPopup(btnCancelar)


                    Thread {
                        val intent = Intent(this, AccederNotificaciones::class.java).apply{
                            putExtra("numeroDeCuenta" , cuenta)
                        }
                        Thread.sleep(50)
                        startActivity(intent)
                    }.start()


                }
                btnAceptar.setOnClickListener {
                    //
                    //
                    //
                    //
                    //autoacceder a notificaciones con el cambio en base de datos para que se actualice la notificacion
                    //hacer que los botones se vuelvan del mismo color del item para que no se vean ademas de que se desactive el clickeo
                    //realizar el respectivo cambio en la base de datos

                    val idExterno = notify["CuentaEnviador"]?.toString() ?: "ID desconocido"

                    var cantidad = (notify["Monto"]?.toString() ?: "Monto desconocido")
                    var cantidad2 = cantidad.toInt()

                    val cantidadDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta).toString().toInt()
                    if(cantidad2 <= cantidadDisponible){
                        bdHelper.actualizarSaldoCuenta(cuenta.toLong() , cantidad2.toLong() *-1)

                        val band = bdHelper.actualizarSaldoPorTelefono( cantidad2.toString() , idExterno)
//                        Toast.makeText(this, "ID externo " + idExterno + " y las filas afectadas son " + band, Toast.LENGTH_SHORT).show()

                        val idMensaje = notify["ID"]?.toString() ?: "ID desconocido"
                        val nuevoEstado = "2"
                        bdHelper.actualizarEstadoDeUNMensaje(idMensaje, nuevoEstado)

                        //Insertar un movimiento / envio

                        val cuentaRecibir = idExterno
                        val monto = cantidad2
                        val mensaje = "Peticion de dinero"
                        val fechaActual = java.time.LocalDate.now().toString()
                        val cuentaEnviador = cuenta.toString()
                        bdHelper.insertarEnvio(cuentaRecibir, monto.toString(), mensaje, fechaActual, cuentaEnviador)

                        //Insertar mensaje


                        val tipo = "repuesta Peticion"
                        val mensajeM = "Su peticion ha sido aceptada."
                        //fecha actual que se tiene ariiva fechaActual
                        //consiguiendo el id del usuario acctual
                        val usuarioID = bdHelper.obtenerUsuarioIdPorCuentaId(cuenta).toString()
                        val usaurioExternoId = idExterno
                        //monto que ya se tiene
                        val estado = "1"

                        bdHelper.insertarMensaje(tipo, mensajeM, fechaActual, usaurioExternoId, usuarioID, monto.toString() , estado)

                        val popupHelper = PopupHelperConfirm(this)
                        popupHelper.showConfirmationPopup(btnAceptar)

                        Thread {
                            val intent = Intent(this, AccederNotificaciones::class.java).apply{
                                putExtra("numeroDeCuenta" , cuenta)
                            }
                            Thread.sleep(50)
                            startActivity(intent)
                        }.start()
                    }else {
                        Toast.makeText(this, "No tiene fondos suficientes", Toast.LENGTH_SHORT)
                            .show()
                        val popupHelper = PopupHelperCancel(this)
                        popupHelper.showConfirmationPopup(btnAceptar)

                        Handler(Looper.getMainLooper()).postDelayed({
                            val intent = Intent(this, AccederNotificaciones::class.java).apply {
                                putExtra("numeroDeCuenta", cuenta)
                            }
                            startActivity(intent)
                        }, 100)

                    }







                }

                // Establece el evento OnClick para cada item
                itemView.setOnClickListener {
                   // Toast.makeText(this, "ID del usuario es " + idUsuario, Toast.LENGTH_SHORT).show()
                   // Toast.makeText(this, "Nombre de usuario " + nombreUsiarioPide, Toast.LENGTH_SHORT).show()
                    // Cierra el PopupWindow actual antes de abrir uno nuevo
                    val referencia = notify["ID"]?.toString() ?: "ID desconocido"
                    val popupHelper = PopupDetallesDelMovimientoMovimientos(this)
                    popupHelper.showPopup(it, referencia , cuenta , true)
                }

                // Agrega el FrameLayout al LinearLayout del ScrollView
                linearLayout.addView(itemView)
            }
        }



    }


    override fun onBackPressed() {
        //se busca sobreescribir el evento de retroceso con el boton hacia atras del celular
        Toast.makeText(this, "Prueba usando los botones de control de Nequi", Toast.LENGTH_SHORT).show()
    }

}