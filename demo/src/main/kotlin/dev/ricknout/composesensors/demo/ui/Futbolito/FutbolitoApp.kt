package dev.ricknout.composesensors.demo.ui.Futbolito

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.ricknout.composesensors.accelerometer.rememberAccelerometerSensorValueAsState
import kotlin.math.abs

@Composable
fun FutbolitoApp() {
    // Estados de las puntuaciones
    var scoreTop by remember { mutableStateOf(0) }
    var scoreBottom by remember { mutableStateOf(0) }

    // Configuración de pantalla y densidad
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val width = with(density) { configuration.screenWidthDp.dp.toPx() }
    val height = with(density) { configuration.screenHeightDp.dp.toPx() }

    // Estado para la posición y velocidad de la pelota
    var ballPosition by remember { mutableStateOf(Offset(width / 2, height / 2)) }
    var velocityX by remember { mutableStateOf(0f) }
    var velocityY by remember { mutableStateOf(0f) }

    // Dimensión del radio de la pelota
    val radius = with(density) { 20.dp.toPx() }

    // Acceso al acelerómetro
    val sensorValue by rememberAccelerometerSensorValueAsState()
    val (x, y, _) = sensorValue.value

    // Actualizar posición de la pelota y manejar rebotes
    LaunchedEffect(sensorValue) {
        // Ajustar la velocidad según el acelerómetro (invierte eje x para orientación)
        velocityX = -x * 30 // Ajustar sensibilidad y dirección
        velocityY = y * 30

        // Actualizar posición de la pelota
        ballPosition = Offset(
            x = ballPosition.x + velocityX,
            y = ballPosition.y + velocityY
        )

        // Manejar rebotes en los bordes
        if (ballPosition.x <= radius) {
            velocityX = abs(velocityX) // Rebota hacia la derecha
            ballPosition = ballPosition.copy(x = radius) // Ajustar posición dentro de límites
        }
        if (ballPosition.x >= width - radius) {
            velocityX = -abs(velocityX) // Rebota hacia la izquierda
            ballPosition = ballPosition.copy(x = width - radius)
        }
        if (ballPosition.y <= radius) {
            velocityY = abs(velocityY) // Rebota hacia abajo
            ballPosition = ballPosition.copy(y = radius)
        }
        if (ballPosition.y >= height - radius) {
            velocityY = -abs(velocityY) // Rebota hacia arriba
            ballPosition = ballPosition.copy(y = height - radius)
        }

        // Detectar goles
        if (ballPosition.y <= radius && ballPosition.x in (width / 2 - 100)..(width / 2 + 100)) {
            scoreTop++ // Gol superior
            resetBall(width, height).let { ballPosition = it } // Reiniciar pelota
            velocityX = 0f
            velocityY = 0f
        }
        if (ballPosition.y >= height - radius && ballPosition.x in (width / 2 - 100)..(width / 2 + 100)) {
            scoreBottom++ // Gol inferior
            resetBall(width, height).let { ballPosition = it } // Reiniciar pelota
            velocityX = 0f
            velocityY = 0f
        }
    }

    // Interfaz de usuario
    Column(modifier = Modifier.fillMaxSize()) {
        // Marcador
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Superior: $scoreTop", style = MaterialTheme.typography.headlineMedium)
            Text("Inferior: $scoreBottom", style = MaterialTheme.typography.headlineMedium)
        }

        // Cancha
        Box(modifier = Modifier.fillMaxSize()) {
            Cancha(
                ballPosition = ballPosition,
                scoreTop = scoreTop,
                scoreBottom = scoreBottom
            )
        }
    }
}

// Función para reiniciar la posición de la pelota
fun resetBall(width: Float, height: Float): Offset {
    return Offset(width / 2, height / 2)
}



//// Manejar rebotes en los bordes
//if (ballPosition.x <= radius) {
//    velocityX = -abs(velocityX) * 0.8f // Rebota hacia la derecha y reduce velocidad en un 20%
//    ballPosition = ballPosition.copy(x = radius) // Ajustar posición dentro de límites
//}
//if (ballPosition.x >= width - radius) {
//    velocityX = -abs(velocityX) * 0.8f // Rebota hacia la izquierda y reduce velocidad en un 20%
//    ballPosition = ballPosition.copy(x = width - radius)
//}
//if (ballPosition.y <= radius) {
//    velocityY = -abs(velocityY) * 0.8f // Rebota hacia abajo y reduce velocidad en un 20%
//    ballPosition = ballPosition.copy(y = radius)
//}
//if (ballPosition.y >= height - radius) {
//    velocityY = -abs(velocityY) * 0.8f // Rebota hacia arriba y reduce velocidad en un 20%
//    ballPosition = ballPosition.copy(y = height - radius)
//}
