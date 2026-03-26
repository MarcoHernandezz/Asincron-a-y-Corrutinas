package com.example.apijson

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MiApiViewModel : ViewModel() {
    // Estado para la UI usando LiveData para XML
    val resultadoTexto = MutableLiveData<String>().apply { value = "Cargando personajes... " }

    // Configuración de Retrofit
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://thronesapi.com/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(ApiService::class.java)

    init {
        llamarApi()
    }

    private fun llamarApi() {
        viewModelScope.launch {
            try {
                val personajes = api.obtenerPersonajes()
                if (personajes.isNotEmpty()) {
                    val p = personajes[0]
                    resultadoTexto.postValue("Primer personaje: ${p.fullName} (${p.title}) de la casa ${p.family}")
                } else {
                    resultadoTexto.postValue("No se encontraron personajes.")
                }
            } catch (e: Exception) {
                resultadoTexto.postValue("Error de conexión: ${e.localizedMessage}")
            }
        }
    }
}