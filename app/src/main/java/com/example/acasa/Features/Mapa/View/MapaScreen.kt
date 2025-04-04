package com.example.acasa.features.mapa.view

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.MapView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.acasa.features.mapa.viewmodel.MapaViewModel
import com.example.acasa.utils.RequestLocationPermission


@Composable
fun MapaScreen(context: Context, mapaViewModel: MapaViewModel = viewModel()) {
    // Estado de la ubicación del usuario
    val userLocation by mapaViewModel.userLocation.collectAsState()

    // Pedir permisos antes de obtener la ubicación
    RequestLocationPermission(
        context = context,
        onPermissionGranted = {
            println("Permisos concedidos, obteniendo ubicación...")
            mapaViewModel.fetchUserLocation(context)
        },
        onPermissionDenied = {
            println("Permisos denegados, no se puede obtener la ubicación")
        }
    )

    // Mueve el mapa cuando userLocation cambie
    var mapView: MapView? by remember { mutableStateOf(null) }

    LaunchedEffect(userLocation) {
        userLocation?.let {
            mapView?.controller?.setCenter(GeoPoint(it.latitude, it.longitude))

            // Agregar marcador
            val marker = Marker(mapView).apply {
                position = GeoPoint(it.latitude, it.longitude)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "Estás aquí"
            }
            mapView?.overlays?.add(marker)
            mapView?.invalidate()
        }
    }

    AndroidView(factory = { ctx ->
        Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", 0))
        MapView(ctx).apply {
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            mapView = this

            // Overlay de ubicación en tiempo real
            val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), this).apply {
                enableMyLocation()
            }
            overlays.add(locationOverlay)
        }
    }, modifier = Modifier.fillMaxSize())
}
