package com.example.acasa.Data.Repository

import androidx.compose.runtime.mutableStateListOf
import com.example.acasa.Data.Model.Ruta

object RutasRepository {
    private val _rutasGuardadas = mutableStateListOf<Ruta>()
    val rutasGuardadas: List<Ruta> get() = _rutasGuardadas

    fun guardarRuta(ruta: Ruta) {
        _rutasGuardadas.add(ruta)
    }
}
