package com.example.acasa.utils


import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.*
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper

object LocationUtils {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(
        context: Context,
        onSuccess: (Location) -> Unit,
        onError: (Exception) -> Unit
    ) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(1000) // Cada 1 segundo
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY) // Usa GPS
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    println("Ubicacion actualizada: Lat=${location.latitude}, Lng=${location.longitude}")
                    onSuccess(location)
                    fusedLocationClient.removeLocationUpdates(this) // Detiene para ahorrar batería
                } ?: onError(Exception("Ubicacion no encontrada :("))
            }
        }

        // Solicitar actualizaciones activas de ubicacion
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
}
