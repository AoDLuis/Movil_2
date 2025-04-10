package com.example.acasa.Features.Mapa.View

import androidx.compose.foundation.layout.*
import androidx.compose.material.SnackbarHostState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.acasa.features.mapa.viewmodel.MapaViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import org.osmdroid.util.GeoPoint

@Composable
fun RutasRecientesScreen(
    navController: NavController,
    viewModel: MapaViewModel = viewModel(),
    scaffoldState: SnackbarHostState = remember { SnackbarHostState() }
) {
    var direccion by remember { mutableStateOf("") }
    val colorScheme = MaterialTheme.colorScheme
    val typography = MaterialTheme.typography

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Caja de búsqueda editable
            SearchAddressBoxEditable(
                text = direccion,
                onTextChange = { direccion = it },
                onSearch = {
                    if (direccion.isNotBlank()) {
                        viewModel.buscarDireccion(
                            direccion,
                            onSuccess = { lat, lon ->
                                val geoPoint = GeoPoint(lat, lon)
                                val latStr = geoPoint.latitude.toString().replace(",", ".")
                                val lonStr = geoPoint.longitude.toString().replace(",", ".")
                                navController.navigate("mapaScreen/$latStr/$lonStr")
                            },
                            onError = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    scaffoldState.showSnackbar("Dirección no encontrada.")
                                }
                            }
                        )
                    }
                }
            )

            // Título de rutas recientes
            if (viewModel.rutasRecientes.isNotEmpty()) {
                Text(
                    text = "Rutas recientes",
                    style = typography.titleMedium,
                    color = colorScheme.onBackground
                )
            }

            // Lista de botones por cada ruta reciente
            viewModel.rutasRecientes.forEachIndexed { index, ruta ->
                Button(
                    onClick = {
                        val latStr = ruta.destino.latitude.toString().replace(",", ".")
                        val lonStr = ruta.destino.longitude.toString().replace(",", ".")
                        navController.navigate("mapaScreen/$latStr/$lonStr")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Ruta ${index + 1}: " +
                                "Desde (${ruta.inicio.latitude.format(4)}, ${ruta.inicio.longitude.format(4)}) " +
                                "→ Hasta (${ruta.destino.latitude.format(4)}, ${ruta.destino.longitude.format(4)})"
                    )
                }
            }
        }
    }
}

private fun Double.format(digits: Int) = "%.${digits}f".format(this)
