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
    
    // Lista completa para la UI
    private var _listaPersonajes: List<Personaje> = emptyList()
    val listaPersonajesNombres: List<String>
        get() = _listaPersonajes.mapIndexed { index, personaje -> "${index + 1}. ${personaje.fullName}" }

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
                _listaPersonajes = api.obtenerPersonajes()
                if (_listaPersonajes.isNotEmpty()) {
                    mostrarPersonaje(0)
                }
            } catch (e: Exception) {
                errorMsg.postValue("Error: ${e.localizedMessage}")
            }
        }
    }

    fun siguientePersonaje() {
        if (_listaPersonajes.isNotEmpty()) {
            indiceActual = (indiceActual + 1) % _listaPersonajes.size
            mostrarPersonaje(indiceActual)
        }
    }

    fun anteriorPersonaje() {
        if (_listaPersonajes.isNotEmpty()) {
            indiceActual = if (indiceActual - 1 < 0) _listaPersonajes.size - 1 else indiceActual - 1
            mostrarPersonaje(indiceActual)
        }
    }

    fun seleccionarPersonaje(index: Int) {
        if (index in _listaPersonajes.indices) {
            indiceActual = index
            mostrarPersonaje(indiceActual)
        }
    }

    private fun mostrarPersonaje(index: Int) {
        personajeActual.postValue(_listaPersonajes[index])
    }
}