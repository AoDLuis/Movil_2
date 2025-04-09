package com.example.acasa.Data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClienteGeocod {

    val geocoding: GeocodingAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocodingAPI::class.java)
    }

}