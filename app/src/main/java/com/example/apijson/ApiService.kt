package com.example.apijson

import retrofit2.http.GET

interface ApiService {
    @GET("Characters")
    suspend fun obtenerPersonajes(): List<Personaje>
}