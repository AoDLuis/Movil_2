package com.example.acasa.features.mapa.viewmodel

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.acasa.utils.LocationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.osmdroid.util.GeoPoint

class MapaViewModel : ViewModel() {

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation


    var homeLocation = mutableStateOf<GeoPoint?>(null)

    fun setHomeLocation(point: GeoPoint) {
        homeLocation.value = point
    }

    fun fetchUserLocation(context: Context) {
        LocationUtils.getCurrentLocation(
            context,
            onSuccess = { location ->
                _userLocation.value = location
            },
            onError = { e ->
                Log.e("MapaViewModel", "Error obteniendo ubicación", e)
            }
        )
    }


}
