package com.example.acasa.features.mapa.view

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.acasa.Data.Model.Ruta
import com.example.acasa.Data.Model.Step
import com.example.acasa.Data.Repository.RutasRepository.guardarRuta
import com.example.acasa.Features.Mapa.View.SearchAddressBox
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaScreen(
    context: Context,
    navController: NavHostController,
    destinoMarcado: GeoPoint? = null,
    mapaViewModel: MapaViewModel = viewModel())
{
    val userLocation by mapaViewModel.userLocation.collectAsState()

    var selectedPoint by remember { mutableStateOf<GeoPoint?>(null) }
    var isConfirmed by remember { mutableStateOf(false) }
    var routePoints by remember { mutableStateOf<List<GeoPoint>>(emptyList()) }
    var routeSteps by remember { mutableStateOf<List<Step>>(emptyList()) }
    var selectedProfile by remember { mutableStateOf("") }
    var mapView: MapView? by remember { mutableStateOf(null) }
    val scaffoldState = rememberBottomSheetScaffoldState()
    var casaMarker by remember { mutableStateOf<Marker?>(null) }




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


    LaunchedEffect(destinoMarcado) {
        destinoMarcado?.let { destino ->
            selectedPoint = destino
            isConfirmed = false
            mapView?.controller?.setCenter(destino)

            // Quitar marcador anterior
            casaMarker?.let { mapView?.overlays?.remove(it) }

            val marker = Marker(mapView).apply {
                position = destino
                title = "Casa"
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            }
            casaMarker = marker
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

                                // -------------------------------------Dibuja la ruta en el mapa
                                mapView?.overlays?.add(Polyline().apply {
                                    setPoints(route)
                                })
                                mapView?.invalidate()

                                // ------------------- Geocodificar ambos puntos
                                mapaViewModel.obtenerDirecciones(start, end) { dirInicio, dirDestino ->
                                    val ruta = Ruta(
                                        inicio = start,
                                        destino = end,
                                        direccionInicio = dirInicio,
                                        direccionDestino = dirDestino
                                    )

                                    guardarRuta(ruta)
                                    mapaViewModel.agregarRutaReciente(ruta)
                                }
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
                    routeSteps = emptyList()

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
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

                                // 💥 Limpia la ruta anterior y las instrucciones
                                routeSteps = emptyList()
                                routePoints = emptyList()
                                mapView?.overlays?.removeAll { it is Polyline }

                                // 💥 Borra marcador anterior si existe
                                overlays.removeAll { it is Marker && it.title == "Casa" }
                                casaMarker?.let { overlays.remove(it) }

                                // 🧷 Crea nuevo marcador
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
            }, modifier = Modifier.fillMaxSize()
            )

            // Barra de búsqueda encima, en la parte superior
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                SearchAddressBox(
                    onClick = {
                        navController.navigate("rutasRecientes")
                    }
                )

            }

        }
    }
}