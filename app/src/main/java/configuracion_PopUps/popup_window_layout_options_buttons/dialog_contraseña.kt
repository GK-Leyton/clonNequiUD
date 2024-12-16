import android.content.Context
import android.os.Bundle
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
import com.example.nequi_proyecto_componentes.R
import configuracion_PopUps.popup_window_layout_options_buttons.DatabaseHelper
import configuracion_PopUps.popup_window_layout_options_buttons.PopupHelperSeguridad_parte1

class DialogConfirmacionContraseña (cuenta: String) : DialogFragment() {

    private lateinit var passwordField1: EditText
    private lateinit var passwordField2: EditText
    private lateinit var passwordField3: EditText
    private lateinit var passwordField4: EditText
    private  var cuentaGlobal  = cuenta
    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.login_contrasena, null)

        passwordField1 = view.findViewById(R.id.editTextNumberPassword1)
        passwordField2 = view.findViewById(R.id.editTextNumberPassword2)
        passwordField3 = view.findViewById(R.id.editTextNumberPassword3)
        passwordField4 = view.findViewById(R.id.editTextNumberPassword4)

        setupPasswordFieldFocus(passwordField1, passwordField2)
        setupPasswordFieldFocus(passwordField2, passwordField3)
        setupPasswordFieldFocus(passwordField3, passwordField4)
        setupPasswordFieldFocus(passwordField4, null)

        val btnVolver = view.findViewById<ImageButton>(R.id.btnVolver1)
        btnVolver.setOnClickListener {
            dismiss()
        }

        builder.setView(view)
        val dialog = builder.create()

        // Ajustar el color de fondo del diálogo
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.decorView?.setBackgroundColor(android.graphics.Color.parseColor("#1E001F"))

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

            val contrasena = passwordField1.text.toString() + passwordField2.text.toString() + passwordField3.text.toString() + passwordField4.text.toString()
            val bdHelper = DatabaseHelper(requireContext())
            val res = bdHelper.obtenerContrasenaPorID(cuentaGlobal)

            if(contrasena.equals(res.toString())){
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(passwordField1.windowToken, 0)
                dialog?.dismiss()

                // Proporciona el FragmentManager al constructor
                val popupHelper = PopupHelperSeguridad_parte1(requireContext(), parentFragmentManager)
                popupHelper.showPopup(passwordField1 , cuentaGlobal)
            }else{
                Toast.makeText(requireContext(), "Contraseña incorrecta o valor invalido", Toast.LENGTH_SHORT).show()
            }



        }
    }

    companion object {
        fun newInstance(cuenta: String): DialogConfirmacionContraseña {

            return DialogConfirmacionContraseña(cuenta)
        }
    }
}
