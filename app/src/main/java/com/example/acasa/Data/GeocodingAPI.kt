package com.example.acasa.Data

import com.example.acasa.Data.Model.GeocodingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPI { //con nonatim
    @GET("search")
    fun searchLocation(
        @Query("q") query: String,
        @Query("format") format: String = "json"
    ): Call<List<GeocodingResponse>>

    @GET("reverse?format=json")
    fun reverseGeocode(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<GeocodingResponse>
}


