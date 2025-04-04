package com.example.acasa.Data.Model

data class RouteResponse(
    val routes: List<Route>
)

data class Route(
    val geometry: String // La ruta codificada en formato GeoJSON
)