package com.juanite.parejas_juanhermo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textview.MaterialTextView
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var txt_brand: MaterialTextView
    private lateinit var txt_model: MaterialTextView
    private lateinit var txt_brandList: TextView
    private lateinit var txt_modelList: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        txt_brand = findViewById(R.id.txt_brand)
        txt_model = findViewById(R.id.txt_model)
        txt_brandList = findViewById(R.id.txt_brandList)
        txt_modelList = findViewById(R.id.txt_modelList)
    }

    private fun obtenerTextoDesdeEditText(editText: MaterialTextView): String {
        return editText.text.toString()
    }

    private fun mostrarAviso(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun datoExistente(dato: String): Boolean {
        return sharedPreferences.contains(dato)
    }

    fun guardarDatoValor(view: View) {
        val brand = obtenerTextoDesdeEditText(txt_brand)
        val model = obtenerTextoDesdeEditText(txt_model)

        if (brand.isNotEmpty() && model.isNotEmpty()) {
            val dato = JSONObject()
            dato.put("brand", brand)
            dato.put("model", model)

            val datoString = dato.toString()

            if (datoExistente(datoString)) {
                mostrarAviso("La pareja de clave-valor ya existe. Actualiza o modifica el nombre del dato.")
            } else {
                editor.putString(brand, datoString)  // Usar "brand" como clave en lugar de datoString
                editor.apply()
                mostrarAviso("Pareja de clave-valor guardada exitosamente.")
            }
        } else {
            mostrarAviso("Ingresa valores válidos para Brand y Model.")
        }
    }

    fun buscarDato(view: View) {
        val brand = obtenerTextoDesdeEditText(txt_brand)
        val model = obtenerTextoDesdeEditText(txt_model)

        val valorEncontrado = sharedPreferences.getString(brand, null)  // Usar "brand" como clave

        if (valorEncontrado != null) {
            val datoEncontrado = JSONObject(valorEncontrado)

            txt_brandList.text = datoEncontrado.getString("brand")
            txt_modelList.text = datoEncontrado.getString("model")

            // Setear el modelo encontrado en el TextInputEditText
            txt_model.setText(datoEncontrado.getString("model"))
            mostrarAviso("Valor encontrado: $valorEncontrado")
        } else {
            // Limpiar el TextInputEditText si el valor no se encuentra
            txt_model.text = null
            mostrarAviso("El dato no existe.")
        }
    }


    fun borrarDato(view: View) {
        val brand = obtenerTextoDesdeEditText(txt_brand)
        val model = obtenerTextoDesdeEditText(txt_model)

        val datoABorrar = JSONObject()
        datoABorrar.put("brand", brand)
        datoABorrar.put("model", model)

        val datoABorrarString = datoABorrar.toString()

        if (datoExistente(datoABorrarString)) {
            editor.remove(datoABorrarString)
            editor.apply()
            mostrarAviso("Dato borrado exitosamente.")
        } else {
            mostrarAviso("El dato no existe.")
        }
    }

    fun modificarValorDato(view: View) {
        val brand = obtenerTextoDesdeEditText(txt_brand)
        val model = obtenerTextoDesdeEditText(txt_model)

        val datoAModificar = JSONObject()
        datoAModificar.put("brand", brand)
        datoAModificar.put("model", model)

        val nuevoBrand = obtenerTextoDesdeEditText(txt_brand)
        val nuevoValor = JSONObject()
        nuevoValor.put("brand", nuevoBrand)
        nuevoValor.put("model", model)

        val datoAModificarString = datoAModificar.toString()
        val nuevoValorString = nuevoValor.toString()

        if (datoExistente(datoAModificarString)) {
            editor.remove(datoAModificarString)
            editor.putString(nuevoValorString, nuevoValorString)
            editor.apply()
            mostrarAviso("Valor del dato actualizado.")
        } else {
            mostrarAviso("El dato no existe.")
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