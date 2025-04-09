package com.example.acasa.features.mapa.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.acasa.Data.Model.Step
import com.example.acasa.Features.Mapa.View.SheetContent
import com.example.acasa.features.mapa.viewmodel.MapaViewModel
import com.example.acasa.utils.RequestLocationPermission
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.MapEventsOverlay
import retrofit2.Call



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaScreen(context: Context, mapaViewModel: MapaViewModel = viewModel()) {
    val userLocation by mapaViewModel.userLocation.collectAsState()

    var selectedPoint by remember { mutableStateOf<GeoPoint?>(null) }
    var isConfirmed by remember { mutableStateOf(false) }
    var routePoints by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    var routeSteps by remember { mutableStateOf<List<Step>>(emptyList()) }


    var selectedProfile by remember { mutableStateOf("") }
    var mapView: MapView? by remember { mutableStateOf(null) }

    val scaffoldState = rememberBottomSheetScaffoldState()
    var casaMarker: Marker? = null



    RequestLocationPermission(
        context = context,
        onPermissionGranted = {
            mapaViewModel.fetchUserLocation(context)
        },
        onPermissionDenied = {
            println("Permisos denegados")
        }
    )

    LaunchedEffect(userLocation) {
        userLocation?.let {
            mapView?.controller?.setCenter(GeoPoint(it.latitude, it.longitude))
            val marker = Marker(mapView).apply {
                position = GeoPoint(it.latitude, it.longitude)
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                title = "Estás aquí"
            }
            mapView?.overlays?.add(marker)
            mapView?.invalidate()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 80.dp,
        sheetContent = {
            SheetContent(
                selectedProfile = selectedProfile,
                onSelectProfile = { selectedProfile = it },
                selectedPoint = selectedPoint,
                isConfirmed = isConfirmed,
                onConfirm = {
                    isConfirmed = true
                    mapaViewModel.setHomeLocation(selectedPoint!!)
                },
                onDrawRoute = {
                    userLocation?.let { location ->
                        val start = GeoPoint(location.latitude, location.longitude)
                        val end = selectedPoint!!

                        mapaViewModel.fetchRoute(
                            profile = selectedProfile,
                            start = start,
                            end = end,
                            onResult = { route, steps ->
                                routePoints = route
                                routeSteps = steps
                                mapView?.overlays?.add(Polyline().apply {
                                    setPoints(route)
                                })
                                mapView?.invalidate()
                            },
                            onError = { error ->
                                println("Error al obtener ruta: ${error.message}")
                            }
                        )

                    }
                },
                onResetRoute = {
                    selectedPoint = null
                    isConfirmed = false
                    routePoints = emptyList()

                    casaMarker?.let {
                        mapView?.overlays?.remove(it)
                        casaMarker = null
                    }

                    mapView?.overlays?.removeAll { it is Polyline }
                    mapView?.invalidate()
                },
                routePoints = routePoints,
                routeSteps = routeSteps //
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {

            AndroidView(factory = { ctx ->
                Configuration.getInstance().load(ctx, ctx.getSharedPreferences("osmdroid", 0))
                MapView(ctx).apply {
                    setMultiTouchControls(true)
                    controller.setZoom(15.0)
                    mapView = this

                    val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(ctx), this).apply {
                        enableMyLocation()
                    }
                    overlays.add(locationOverlay)

                    val tapOverlay = MapEventsOverlay(object : MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                            p?.let {
                                selectedPoint = it
                                isConfirmed = false
                                overlays.removeAll { it is Marker && it.title == "Casa" }

                                casaMarker?.let { overlays.remove(it) }

                                val marker = Marker(this@apply).apply {
                                    position = it
                                    title = "Casa"
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                    isDraggable = true
                                    setOnMarkerDragListener(object : Marker.OnMarkerDragListener {
                                        override fun onMarkerDragStart(marker: Marker?) {}
                                        override fun onMarkerDrag(marker: Marker?) {}
                                        override fun onMarkerDragEnd(marker: Marker?) {
                                            selectedPoint = marker?.position
                                            isConfirmed = false
                                        }
                                    })
                                }
                                casaMarker = marker
                                overlays.add(marker)

                                invalidate()
                            }
                            return true
                        }

                        override fun longPressHelper(p: GeoPoint?) = false
                    })
                    overlays.add(tapOverlay)
                }
            }, modifier = Modifier.fillMaxSize())

        }
    }
}




