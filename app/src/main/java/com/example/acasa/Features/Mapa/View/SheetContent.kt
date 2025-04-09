package com.example.acasa.Features.Mapa.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.acasa.Data.Model.Step
import org.osmdroid.util.GeoPoint

@Composable
fun SheetContent(
    selectedProfile: String,
    onSelectProfile: (String) -> Unit,
    selectedPoint: GeoPoint?,
    isConfirmed: Boolean,
    onConfirm: () -> Unit,
    onDrawRoute: () -> Unit,
    onResetRoute: () -> Unit,
    routePoints: List<GeoPoint>,
    routeSteps: List<Step>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("¿Cómo te desplazas?", style = MaterialTheme.typography.titleMedium)
        SegmentedButton(
            selected = selectedProfile,
            onSelect = onSelectProfile
        )

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
                enabled = routePoints.isEmpty(),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Trazar ruta")
            }
        }
        Divider()

        if (routePoints.isNotEmpty()) {
            Text(
                text = "Cancela la ruta para volver a trazar otra" +
                        "",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        if (routePoints.isNotEmpty()) {
            Button(
                onClick = onResetRoute,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("✖ Cancelar ruta", color = Color.Black)
            }
        }
        Divider() //falta que se limpien las instrucciones despues de cancelae la ruta
        if (routeSteps.isNotEmpty()) {
            Text("Instrucciones:", style = MaterialTheme.typography.titleMedium)

            routeSteps.forEachIndexed { index, step ->
                Text(
                    text = "${index + 1}. ${step.instruction}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }


    }
}