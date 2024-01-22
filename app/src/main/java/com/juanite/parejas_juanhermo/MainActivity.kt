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

        val dato = "$brand-$model"

        if (datoExistente(dato)) {
            mostrarAviso("La pareja de clave-valor ya existe. Actualiza o modifica el nombre del dato.")
        } else {
            editor.putString(dato, model)
            editor.apply()
            mostrarAviso("Pareja de clave-valor guardada exitosamente.")
        }
    }

    fun buscarDato(view: View) {
        val brand = obtenerTextoDesdeEditText(txt_brand)
        val model = obtenerTextoDesdeEditText(txt_model)

        val datoBuscado = "$brand-$model"
        val valorEncontrado = sharedPreferences.getString(datoBuscado, null)

        if (valorEncontrado != null) {
            val partes = valorEncontrado.split("-")
            txt_brandList.text = partes[0]
            txt_modelList.text = partes[1]
            mostrarAviso("Valor encontrado: $valorEncontrado")
        } else {
            mostrarAviso("El dato no existe.")
        }
    }

    fun borrarDato(view: View) {
        val brand = obtenerTextoDesdeEditText(txt_brand)
        val model = obtenerTextoDesdeEditText(txt_model)

        val datoABorrar = "$brand-$model"

        if (datoExistente(datoABorrar)) {
            editor.remove(datoABorrar)
            editor.apply()
            mostrarAviso("Dato borrado exitosamente.")
        } else {
            mostrarAviso("El dato no existe.")
        }
    }

    fun modificarValorDato(view: View) {
        val brand = obtenerTextoDesdeEditText(txt_brand)
        val model = obtenerTextoDesdeEditText(txt_model)

        val datoAModificar = "$brand-$model"
        val nuevoBrand = obtenerTextoDesdeEditText(txt_brand)
        val nuevoValor = "$nuevoBrand-$model"

        if (datoExistente(datoAModificar)) {
            editor.putString(datoAModificar, nuevoValor)
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
            val partes = key.split("-")
            listaBrand.append("${partes[0]}\n")
            listaModel.append("${partes[1]}\n")
        }

        txt_brandList.text = listaBrand.toString().trimEnd()
        txt_modelList.text = listaModel.toString().trimEnd()

        mostrarAviso("Listado de parejas Dato-Valor mostrado.")
    }
}