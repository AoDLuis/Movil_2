package com.example.acasa.Data.Model

import org.osmdroid.util.GeoPoint

data class Ruta(
    val inicio: GeoPoint,
    val destino: GeoPoint,
    val descripcion: String
)
