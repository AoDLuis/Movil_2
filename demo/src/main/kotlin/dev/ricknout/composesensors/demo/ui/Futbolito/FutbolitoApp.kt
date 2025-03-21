package dev.ricknout.composesensors.demo.ui.Futbolito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    // ----------------------- Acceso al acelerometro -----------------------------------------------------------
    val sensorValue by rememberAccelerometerSensorValueAsState()
    val (x, y, _) = sensorValue.value

    LaunchedEffect(sensorValue) {
        // Ajustar la velocidad según el acelerómetro
        velocityX += -x * 2 // Reducimos el factor para mayor control
        velocityY += y * 2

        var newX = ballPosition.x + velocityX
        var newY = ballPosition.y + velocityY

        // ---------------- Manejo de rebotes ----------------
        if (newX <= radius) {
            newX = radius
            velocityX = -velocityX // Invierte la dirección
        } else if (newX >= width - radius) {
            newX = width - radius
            velocityX = -velocityX
        }

        if (newY <= radius) {
            newY = radius
            velocityY = -velocityY
        } else if (newY >= height - radius) {
            newY = height - radius
            velocityY = -velocityY
        }

        // Actualizar la posición con los valores corregidos
        ballPosition = Offset(newX, newY)

        // Detectar goles
        if (newY <= radius && newX in (width / 2 - 100)..(width / 2 + 100)) {
            scoreTop++
            ballPosition = resetBall(width, height)
            velocityX = 0f
            velocityY = 0f
        }
        if (newY >= height - radius && newX in (width / 2 - 100)..(width / 2 + 100)) {
            scoreBottom++
            ballPosition = resetBall(width, height)
            velocityX = 0f
            velocityY = 0f
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B5E20)), // Color de fondo estilo cancha
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // **Marcador con diseño mejorado**
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color(0xFF0D47A1), shape = RoundedCornerShape(12.dp))
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Superior", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("$scoreTop", color = Color.Yellow, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Inferior", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("$scoreBottom", color = Color.Yellow, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            }
        }

        // **Cancha con la pelota**
        Box(modifier = Modifier.fillMaxSize()) {
            Cancha(
                ballPosition = ballPosition
            )
        }
    }

}

// Función para reiniciar la posición de la pelota
fun resetBall(width: Float, height: Float): Offset {
    return Offset(width / 2, height / 2)
}


