package com.example.acasa.Data

import com.example.acasa.Data.Model.RouteResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenRouteServiceApi {
    @GET("v2/directions/{profile}")
    fun getRoute(
        @retrofit2.http.Path("profile") profile: String,
        @Query("api_key") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("format") format: String = "json"
    ): Call<RouteResponse>
}

