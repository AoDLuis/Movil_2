package com.example.acasa.Utils

import com.example.acasa.Data.APICliente
import com.example.acasa.Data.Model.RouteResponse
import com.example.acasa.Data.Model.Step
import org.osmdroid.util.GeoPoint
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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