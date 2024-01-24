package com.juanite.parejas_juanhermo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var txtInput_brand: TextInputLayout
    private lateinit var txtInput_model: TextInputLayout
    private lateinit var txt_brandList: TextView
    private lateinit var txt_modelList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        txtInput_brand = findViewById(R.id.txtInput_brand)
        txtInput_model = findViewById(R.id.txtInput_model)
        txt_brandList = findViewById(R.id.txt_brandList)
        txt_modelList = findViewById(R.id.txt_modelList)
    }

    private fun obtenerTextoDesdeEditText(editText: EditText?): String {
        if (editText != null) {
            return editText.text.toString()
        }
        return ""
    }

    private fun mostrarAviso(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun datoExistente(clave: String): Boolean {
        return sharedPreferences.contains(clave)
    }

    fun guardarDatoValor(view: View) {
        val brand = obtenerTextoDesdeEditText(txtInput_brand.editText)
        val model = obtenerTextoDesdeEditText(txtInput_model.editText)

        if (brand.isNotEmpty() && model.isNotEmpty()) {
            val claveUnica = brand + model
            val dato = JSONObject()
            dato.put("brand", brand)
            dato.put("model", model)

            val datoString = dato.toString()

            if (datoExistente(claveUnica)) {
                mostrarAviso("La pareja de clave-valor ya existe. Actualiza o modifica el nombre del dato.")
            } else {
                editor.putString(claveUnica, datoString)
                editor.apply()
                mostrarAviso("Pareja de clave-valor guardada exitosamente.")
            }
        } else {
            mostrarAviso("Ingresa valores válidos para Brand y Model.")
        }
    }

    fun buscarDato(view: View) {
        val brand = obtenerTextoDesdeEditText(txtInput_brand.editText)
        val model = obtenerTextoDesdeEditText(txtInput_model.editText)

        val claveUnica = brand + model
        val valorEncontrado = sharedPreferences.getString(claveUnica, null)

        if (valorEncontrado != null) {
            val datoEncontrado = JSONObject(valorEncontrado)

            txt_brandList.text = datoEncontrado.getString("brand")
            txt_modelList.text = datoEncontrado.getString("model")

            txtInput_model.editText?.setText(datoEncontrado.getString("model"))
            mostrarAviso("Valor encontrado: $valorEncontrado")
        } else {
            txtInput_model.editText?.text = null
            mostrarAviso("El dato no existe.")
        }
    }

    fun borrarDato(view: View) {
        val brand = obtenerTextoDesdeEditText(txtInput_brand.editText)
        val model = obtenerTextoDesdeEditText(txtInput_model.editText)

        val claveUnica = brand + model

        if (datoExistente(claveUnica)) {
            editor.remove(claveUnica)
            editor.apply()
            mostrarAviso("Dato borrado exitosamente.")
        } else {
            mostrarAviso("El dato no existe.")
        }
    }

    fun modificarValorDato(view: View) {
        val modelToSearch = obtenerTextoDesdeEditText(txtInput_model.editText)
        val nuevoBrand = obtenerTextoDesdeEditText(txtInput_brand.editText)

        val allEntries = sharedPreferences.all

        var datoModificado = false

        for ((key, value) in allEntries) {
            val dato = JSONObject(value.toString())

            if (dato.getString("model") == modelToSearch) {
                dato.put("brand", nuevoBrand)

                val nuevoValorString = dato.toString()

                editor.putString(key, nuevoValorString)
                editor.apply()

                datoModificado = true
                break
            }
        }

        if (datoModificado) {
            mostrarAviso("Valor del dato actualizado.")
        } else {
            mostrarAviso("No se encontró ningún dato con el modelo proporcionado.")
        }
    }

    fun mostrarListado(view: View) {
        val allEntries = sharedPreferences.all

        val listaBrand = StringBuilder()
        val listaModel = StringBuilder()

        for ((key, value) in allEntries) {
            val dato = JSONObject(value.toString())
            listaBrand.append("${dato.getString("brand")}\n")
            listaModel.append("${dato.getString("model")}\n")
        }

        txt_brandList.text = listaBrand.toString().trimEnd()
        txt_modelList.text = listaModel.toString().trimEnd()

        mostrarAviso("Listado de parejas Dato-Valor mostrado.")
    }
}