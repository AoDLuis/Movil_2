package com.example.acasa.Data


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APICliente {
    private const val BASE_URL = "https://api.openrouteservice.org/"

    val instance: OpenRouteServiceApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(OpenRouteServiceApi::class.java)
    }
}
