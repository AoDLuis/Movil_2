package com.example.acasa.Data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object APICliente {
    private const val BASE_URL = "https://api.openrouteservice.org/"

    val api: OpenRouteServiceApi by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(OpenRouteServiceApi::class.java)
    }


    val geocodingApi: GeocodingAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingAPI::class.java)
    }
}
