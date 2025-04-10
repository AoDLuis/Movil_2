package com.example.acasa.features.mapa.viewmodel

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.acasa.utils.LocationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.acasa.Data.APICliente
import com.example.acasa.Data.Model.GeocodingResponse
import com.example.acasa.Data.Model.RouteResponse
import com.example.acasa.Data.Model.Ruta
import com.example.acasa.Data.Model.Step
import kotlinx.coroutines.flow.StateFlow
import org.osmdroid.util.GeoPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.runtime.mutableStateListOf


class MapaViewModel : ViewModel() {

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation
    var homeLocation = mutableStateOf<GeoPoint?>(null)

    // MutableStateList para recomposición automática
    private val _rutasRecientes = mutableStateListOf<Ruta>()
    val rutasRecientes: List<Ruta> get() = _rutasRecientes

    fun agregarRutaReciente(ruta: Ruta) {
        if (!_rutasRecientes.any {
                it.inicio == ruta.inicio && it.destino == ruta.destino
            }) {
            _rutasRecientes.add(0, ruta)
        }
    }

    fun obtenerDirecciones(
        puntoInicio: GeoPoint,
        puntoDestino: GeoPoint,
        onResult: (String, String) -> Unit
    ) {

        val callInicio = APICliente.geocodingApi.reverseGeocode(puntoInicio.latitude, puntoInicio.longitude)
        val callDestino = APICliente.geocodingApi.reverseGeocode(puntoDestino.latitude, puntoDestino.longitude)

        callInicio.enqueue(object : Callback<GeocodingResponse> {
            override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {
                val dirInicio = response.body()?.display_name ?: "Ubicación desconocida"

                callDestino.enqueue(object : Callback<GeocodingResponse> {
                    override fun onResponse(call: Call<GeocodingResponse>, response: Response<GeocodingResponse>) {
                        val dirDestino = response.body()?.display_name ?: "Ubicación desconocida"
                        onResult(dirInicio, dirDestino)
                    }

                    override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                        onResult(dirInicio, "Ubicación desconocida")
                    }
                })
            }

            override fun onFailure(call: Call<GeocodingResponse>, t: Throwable) {
                onResult("Ubicación desconocida", "Ubicación desconocida")
            }
        })

    }



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

    // ------------------se comunica con la api de rutas :) ------------------------------
    fun fetchRoute(
        profile: String,
        start: GeoPoint,
        end: GeoPoint,
        onResult: (List<GeoPoint>, List<Step>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val api = APICliente.api
        val startStr = "${start.longitude},${start.latitude}"
        val endStr = "${end.longitude},${end.latitude}"

        val call = api.getRoute(
            profile = profile,
            apiKey = "5b3ce3597851110001cf62482f324ffbebe74aebb74c3b3a541c2caf",
            start = startStr,
            end = endStr
        )

        call.enqueue(object : Callback<RouteResponse> {
            override fun onResponse(call: Call<RouteResponse>, response: Response<RouteResponse>) {
                if (response.isSuccessful) {
                    val feature = response.body()?.features?.firstOrNull()
                    val coordinates = feature?.geometry?.coordinates ?: emptyList()
                    val route = coordinates.map { GeoPoint(it[1], it[0]) }
                    val steps = feature?.properties?.segments?.firstOrNull()?.steps ?: emptyList()
                    onResult(route, steps)
                } else {
                    onError(Exception("Error: ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<RouteResponse>, t: Throwable) {
                onError(t)
            }
        })
    }


    fun buscarDireccion(
        direccion: String,
        onSuccess: (lat: Double, lon: Double) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val call = APICliente.geocodingApi.searchLocation(direccion)

        call.enqueue(object : Callback<List<GeocodingResponse>> {
            override fun onResponse(
                call: Call<List<GeocodingResponse>>,
                response: Response<List<GeocodingResponse>>
            ) {
                val result = response.body()?.firstOrNull()
                if (result != null) {
                    val lat = result.lat.toDouble()
                    val lon = result.lon.toDouble()
                    onSuccess(lat, lon)
                } else {
                    onError(Exception("No se encontró la dirección"))
                }
            }

            override fun onFailure(call: Call<List<GeocodingResponse>>, t: Throwable) {
                onError(t)
            }
        })
    }



}
