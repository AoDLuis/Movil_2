package com.example.acasa.Data

import com.example.acasa.Data.Model.RouteResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenRouteServiceApi {
    @GET("v2/directions/driving-car")
    suspend fun getRoute(
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): RouteResponse
}