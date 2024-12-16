package com.example.nequi_proyecto_componentes

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.Spinner
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirm
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperUpsNoTeAlcanza

class PopupHelperEnvioBancos(private val context: Context) {

    fun showPopup(viewAnchor: View , cuenta : String) {
        // Inflar el layout del PopupWindow
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.popup_window_layout_enviar_bancos, null)

        // Crear el PopupWindow
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        // Configurar el PopupWindow
        popupWindow.isFocusable = true
        popupWindow.isOutsideTouchable = true
        popupWindow.setBackgroundDrawable(null)

        // Configurar los elementos dentro del PopupWindow
        val btnVolver: ImageButton = popupView.findViewById(R.id.imageButton15)
        val btnListo: Button = popupView.findViewById(R.id.btnListoEnviarbancos)

        val spinnerTipoDocumento: Spinner = popupView.findViewById(R.id.spinnerTipoDocumento)
        val spinnerBanco: Spinner = popupView.findViewById(R.id.spinner_banco)
        val spinnerTipoCuenta: Spinner = popupView.findViewById(R.id.spinner_tipo_cuenta)

        val nombreRecibe: EditText = popupView.findViewById(R.id.txtNombreRecibe)
        val numeroDocumento: EditText = popupView.findViewById(R.id.txtNumeroDocumento)
        val numeroCuenta: EditText = popupView.findViewById(R.id.txtNumeroCuenta)
        val cuantoEnviar: EditText = popupView.findViewById(R.id.txtNumeroCuantoEnviar)

        // Crear listas de opciones para los Spinners
        val tipoDocumentoOptions = arrayOf("Tipo de documento", "CC", "TI", "NIT")
        val bancoOptions = arrayOf("Banco", "Bancolombia", "Davivienda", "BBVA")
        val tipoCuentaOptions = arrayOf("Tipo de cuenta", "Ahorros", "Corriente", "Otros")

        // Configurar adaptadores para los Spinners
        val tipoDocumentoAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, tipoDocumentoOptions)
        tipoDocumentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDocumento.adapter = tipoDocumentoAdapter

        val bancoAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, bancoOptions)
        bancoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerBanco.adapter = bancoAdapter

        val tipoCuentaAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, tipoCuentaOptions)
        tipoCuentaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoCuenta.adapter = tipoCuentaAdapter

        // Función para habilitar/deshabilitar el botón "Listo"
        fun updateListoButtonState() {
            val tipoDocumentoSelected = spinnerTipoDocumento.selectedItem != null && spinnerTipoDocumento.selectedItem != "Tipo de documento"
            val bancoSelected = spinnerBanco.selectedItem != null && spinnerBanco.selectedItem != "Banco"
            val tipoCuentaSelected = spinnerTipoCuenta.selectedItem != null && spinnerTipoCuenta.selectedItem != "Tipo de cuenta"

            val nombreRecibeValid = nombreRecibe.text.length >= 4
            val numeroDocumentoValid = numeroDocumento.text.length >= 6
            val numeroCuentaValid = numeroCuenta.text.length >= 11

            val cuantoEnviarValid = try {
                cuantoEnviar.text.toString().toFloat() > 50
            } catch (e: NumberFormatException) {
                false
            }

            btnListo.isEnabled = tipoDocumentoSelected && bancoSelected && tipoCuentaSelected &&
                    nombreRecibeValid && numeroDocumentoValid &&
                    numeroCuentaValid && cuantoEnviarValid
        }

        // Configurar los Spinners
        spinnerTipoDocumento.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateListoButtonState()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerBanco.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateListoButtonState()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerTipoCuenta.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                updateListoButtonState()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Configurar los EditText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateListoButtonState()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        nombreRecibe.addTextChangedListener(textWatcher)
        numeroDocumento.addTextChangedListener(textWatcher)
        numeroCuenta.addTextChangedListener(textWatcher)
        cuantoEnviar.addTextChangedListener(textWatcher)

        // Configurar el botón de volver
        btnVolver.setOnClickListener {
            popupWindow.dismiss()
        }

        // Configurar el botón "Listo"
        btnListo.setOnClickListener {

            val bdHelper = DatabaseHelper(context)
            val saldoDisponible = bdHelper.obtenerSaldoDisponibleCuentaPorID(cuenta).toString()


            popupWindow.dismiss()

            val handler = Handler(Looper.getMainLooper())

            if(cuantoEnviar.text.toString().toDouble() <= saldoDisponible.toDouble()){
                val cantidadEnvio = cuantoEnviar.text.toString().toLong()
                bdHelper.actualizarSaldoCuenta(cuenta.toLong() , cantidadEnvio )
                val popupHelper = PopupHelperConfirm(context)
                popupHelper.showConfirmationPopup(btnListo)

                handler.postDelayed({
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    context.startActivity(intent)
                }, 100)

            }else{
                val popupHelper = PopupHelperUpsNoTeAlcanza(context)
                popupHelper.showPopup(btnListo)

                handler.postDelayed({
                    val intent = Intent(context, PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                        putExtra("numeroDeCuenta", cuenta)
                    }
                    context.startActivity(intent)
                }, 100)
            }

        }

        // Mostrar el PopupWindow
        popupWindow.showAtLocation(viewAnchor, android.view.Gravity.CENTER, 0, 0)
    }
}
