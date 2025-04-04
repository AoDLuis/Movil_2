package com.example.acasa.Domain.Repository

import com.example.acasa.Domain.Model.Ruta


interface RutaRepository {
    suspend fun obtenerRuta(inicio: String, destino: String): Ruta
}
