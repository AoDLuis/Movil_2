package com.example.acasa.Data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPI {
    @GET("search")
    fun searchLocation(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): Call<List<GeocodingResult>>
}

data class GeocodingResult(
    val lat: String,
    val lon: String,
    val display_name: String
)
