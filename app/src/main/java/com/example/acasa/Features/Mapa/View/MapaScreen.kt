package com.example.acasa.features.mapa.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaScreen(context: Context, mapaViewModel: MapaViewModel = viewModel()) {
    val userLocation by mapaViewModel.userLocation.collectAsState()

    var selectedPoint by remember { mutableStateOf<GeoPoint?>(null) }
    var isConfirmed by remember { mutableStateOf(false) }
    var routePoints by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    var selectedProfile by remember { mutableStateOf("") }
    var mapView: MapView? by remember { mutableStateOf(null) }

    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()


    var showBottomSheet by remember { mutableStateOf(true) }

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
                        routePoints = listOf(start, end)
                        mapView?.overlays?.add(Polyline().apply {
                            setPoints(routePoints)
                        })
                        mapView?.invalidate()
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
        ) {
            // Aquí va tu AndroidView con el MapView
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

                                val marker = Marker(this@apply).apply {
                                    position = it
                                    title = "Casa"
                                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                                }
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

@Composable
fun SegmentedButton(selected: String, onSelect: (String) -> Unit) {
    val options = listOf(
        "driving-car" to "Auto",
        "cycling-regular" to "Moto",
        "foot-walking" to "A pie"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        options.forEach { (value, label) ->
            val selectedColor = if (selected == value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
            val textColor = if (selected == value) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp)
                    .background(selectedColor, shape = MaterialTheme.shapes.medium)
                    .clickable { onSelect(value) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = label, color = textColor)
            }
        }
    }
}

@Composable
fun SheetContent(
    selectedProfile: String,
    onSelectProfile: (String) -> Unit,
    selectedPoint: GeoPoint?,
    isConfirmed: Boolean,
    onConfirm: () -> Unit,
    onDrawRoute: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("¿Cómo te desplazas?", style = MaterialTheme.typography.titleMedium)
        SegmentedButton(selected = selectedProfile, onSelect = onSelectProfile)

        Divider()

        if (selectedPoint != null && !isConfirmed) {
            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Confirmar dirección")
            }
        }

        if (!isConfirmed) {
            Text(
                text = "Toca en el mapa para seleccionar la ubicación de la casa.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        Divider()

        if (isConfirmed && selectedProfile.isNotEmpty()) {
            Button(
                onClick = onDrawRoute,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Trazar ruta")
            }
        }
    }
}

