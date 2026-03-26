package com.example.apijson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MiApiViewModel : ViewModel() {
    // Estado para el personaje actual
    val personajeActual = MutableLiveData<Personaje?>()
    val errorMsg = MutableLiveData<String>()

    private var listaPersonajes: List<Personaje> = emptyList()
    private var indiceActual = 0

    // Configuración de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://thronesapi.com/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    init {
        cargarTodosLosPersonajes()
    }

    private fun cargarTodosLosPersonajes() {
        viewModelScope.launch {
            try {
                listaPersonajes = api.obtenerPersonajes()
                if (listaPersonajes.isNotEmpty()) {
                    mostrarPersonaje(0)
                }
            } catch (e: Exception) {
                errorMsg.postValue("Error: ${e.localizedMessage}")
            }
        }
    }

    fun siguientePersonaje() {
        if (listaPersonajes.isNotEmpty()) {
            indiceActual = (indiceActual + 1) % listaPersonajes.size
            mostrarPersonaje(indiceActual)
        }
    }

    private fun mostrarPersonaje(index: Int) {
        personajeActual.postValue(listaPersonajes[index])
    }
}