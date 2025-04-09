package com.example.acasa.Features.Mapa.View

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.acasa.Data.Repository.RutasRepository

@Composable
fun RutasRecientesScreen(navController: NavHostController) {
    val rutas = RutasRepository.rutasGuardadas

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Rutas Recientes") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            SearchAddressBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                onClick = {
                    // Aquí puedes poner búsqueda real si quieres
                }
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(rutas) { ruta ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(
                                    "mapa?rutaLat=${ruta.destino.latitude}&rutaLon=${ruta.destino.longitude}"
                                )
                            },
                        shape = MaterialTheme.shapes.medium,
                        tonalElevation = 2.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        Text(
                            text = ruta.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
