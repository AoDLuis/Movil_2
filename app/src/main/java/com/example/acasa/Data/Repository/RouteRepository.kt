package com.example.acasa.Data.Repository

import com.example.acasa.Data.Model.RouteResponse
import com.example.acasa.Data.OpenRouteServiceApi

class RouteRepository(private val api: OpenRouteServiceApi) {
    suspend fun getRoute(start: String, end: String): RouteResponse {
        return api.getRoute("TU_API_KEY", start, end)
    }
}
