import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.nequi_proyecto_componentes.PantallaDeCarga
import com.example.nequi_proyecto_componentes.R
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperCancel
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirm
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperConfirmarCambiarContrasena
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperSeguridad_parte1

class DialogCambioContraseña (cuenta : String) : DialogFragment() {

    private lateinit var passwordField1: EditText
    private lateinit var passwordField2: EditText
    private lateinit var passwordField3: EditText
    private lateinit var passwordField4: EditText
    private var cuentaGlobal = cuenta

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_cambio_de_contrasena, null)

        passwordField1 = view.findViewById(R.id.editTextNumberPassword1Edit)
        passwordField2 = view.findViewById(R.id.editTextNumberPassword2Edit)
        passwordField3 = view.findViewById(R.id.editTextNumberPassword3Edit)
        passwordField4 = view.findViewById(R.id.editTextNumberPassword4Edit)

        setupPasswordFieldFocus(passwordField1, passwordField2)
        setupPasswordFieldFocus(passwordField2, passwordField3)
        setupPasswordFieldFocus(passwordField3, passwordField4)
        setupPasswordFieldFocus(passwordField4, null)

        val btnVolver = view.findViewById<ImageButton>(R.id.btn_volver_cambio_contrasena)
        btnVolver.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        val dialog = builder.create()

        // Ajustar el color de fondo del diálogo
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.decorView?.setBackgroundColor(android.graphics.Color.parseColor("#FFFFFF"))

        return dialog
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog ?: return
        val window = dialog.window ?: return
        val layoutParams = window.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = layoutParams

        // Forzar el enfoque en el primer campo y abrir el teclado virtual
        passwordField1.requestFocus()
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(passwordField1, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupPasswordFieldFocus(currentField: EditText, nextField: EditText?) {
        currentField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1 && nextField != null) {
                    nextField.requestFocus()
                }
                checkAllFieldsFilled() // Verifica si todos los campos están llenos
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before > 0 && count == 0) {
                    when (currentField) {
                        passwordField2 -> passwordField1.requestFocus()
                        passwordField3 -> passwordField2.requestFocus()
                        passwordField4 -> passwordField3.requestFocus()
                    }
                }
            }
        })
    }

    private fun checkAllFieldsFilled() {
        if (passwordField1.text.isNotEmpty() &&
            passwordField2.text.isNotEmpty() &&
            passwordField3.text.isNotEmpty() &&
            passwordField4.text.isNotEmpty()) {

            val bdHelper = DatabaseHelper(requireContext())
            val contrasenaActual = bdHelper.obtenerContrasenaPorID(cuentaGlobal)
            var contrasena = passwordField1.text.toString() + passwordField2.text.toString() + passwordField3.text.toString() + passwordField4.text.toString()


            if(!contrasenaActual.equals(contrasena)){


                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(passwordField1.windowToken, 0)

                val popupHelper = PopupHelperConfirmarCambiarContrasena(requireContext())
                popupHelper.showPopup(passwordField1 , cuentaGlobal , contrasena)

            }else{
                Toast.makeText(requireContext(), "Prueva con otra contraseña", Toast.LENGTH_SHORT).show()

            }



            //dialog?.dismiss()

            //
            //
            //
            //
            //
            /////logica para el cambio de la contraseña
            //
            //
            //
            //

            val contraseñaPropuesta = passwordField1.text.toString() + passwordField2.text.toString() + passwordField3.text.toString() + passwordField4.text.toString()
           /* if(contraseñaPropuesta.equals("1234")){
                val popupHelper = PopupHelperCancel(requireContext())
                popupHelper.showConfirmationPopup(passwordField1)


                val handler = Handler(Looper.getMainLooper()) // Asegura que el código corre en el hilo principal
                handler.postDelayed({
                    val intent = Intent(requireActivity(), PantallaDeCarga::class.java).apply {
                        putExtra("proximaPagina", "menu_principal")
                    }
                    requireActivity().startActivity(intent)
                    dialog?.dismiss() // Cerrar el diálogo después de iniciar la actividad
                }, 500)

            }else{
                val popupHelper = PopupHelperConfirmarCambiarContrasena(requireContext())
                popupHelper.showPopup(passwordField1)
            }*/



        }
    }

    companion object {
        fun newInstance(cuenta: String): DialogCambioContraseña {
            return DialogCambioContraseña(cuenta)
        }
    }
}
