package dev.ricknout.composesensors.demo.ui.Futbolito

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun FutbolitoApp() {


   //--------------------pantalla -------------------------------------------------------------------------------
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val width = with(density) { configuration.screenWidthDp.dp.toPx() }
    val height = with(density) { configuration.screenHeightDp.dp.toPx() }

    var ballPosition by remember { mutableStateOf(Offset(width / 2, height / 2)) }
    var velocityX by remember { mutableStateOf(0f) }
    var velocityY by remember { mutableStateOf(0f) }
    var scoreTop by remember { mutableStateOf(0) }
    var scoreBottom by remember { mutableStateOf(0) }

    // radio de la pelota
    val radius = with(density) { 20.dp.toPx() }
    val canchaHeight = height - with(density) { 70.dp.toPx() }


    // ----------------------- Acceso al acelerometro -----------------------------------------------------------
    val sensorValue by rememberAccelerometerSensorValueAsState()
    val (x, y, _) = sensorValue.value

    LaunchedEffect(sensorValue) {
        // //--------------------------la velocida de la pelota------------------------------------------------
        velocityX += -x * 5
        velocityY += y * 5

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
        } else if (newY + radius >= canchaHeight) {
            newY = canchaHeight - radius
            velocityY = -velocityY
        }



        //
        ballPosition = Offset(newX, newY)

        // ----------------------------- goles ------------------------------------
        if (newY <= radius && newX in (width / 2 - 100)..(width / 2 + 100)) {
            scoreTop++
            ballPosition = resetBall(width, canchaHeight)
            velocityX = 0f
            velocityY = 0f
        }
        if (newY >= canchaHeight - radius && newX in (width / 2 - 100)..(width / 2 + 100)) {
            scoreBottom++
            ballPosition = resetBall(width, canchaHeight)
            velocityX = 0f
            velocityY = 0f
        }
    }

    ///-------------------------------- el marcador y la cancha------------------------------------------------
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B5E20)), // Color de fondo estilo cancha
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //----el marcador
        Row(
            modifier = Modifier .height(110.dp)
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

        // /-----------------------Cancha con la pelota ---------------------------------------------
        Box(modifier = Modifier.fillMaxSize() .weight(1f) ) {
            Cancha(
                ballPosition = ballPosition
            )
        }
    }

}

// /--------------la pelota vuelve en donde empezo -----------------------------
fun resetBall(width: Float, height: Float): Offset {
    return Offset(width / 2, height / 2)
}


