package com.example.acasa.Data


object APICliente {
    private const val BASE_URL = "https://api.openrouteservice.org/"

    val api: OpenRouteServiceApi by lazy {
        retrofit2.Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .build()
            .create(OpenRouteServiceApi::class.java)
    }
}
