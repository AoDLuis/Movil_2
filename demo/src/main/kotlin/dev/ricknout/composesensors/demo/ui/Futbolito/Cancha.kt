package dev.ricknout.composesensors.demo.ui.Futbolito

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Cancha(
    ballPosition: Offset,
    scoreTop: Int, // Puntuación de la portería superior
    scoreBottom: Int // Puntuación de la portería inferior
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Dibujar pelota
        drawCircle(
            color = Color.Red,
            radius = 20.dp.toPx(),
            center = ballPosition
        )

        // Dibujar portería superior
        drawRect(
            color = Color.Green,
            topLeft = Offset(size.width / 2 - 100, 0f), // Centro horizontal y en la parte superior
            size = Size(200f, 50f) // Dimensiones de la portería
        )

        // Dibujar portería inferior
        drawRect(
            color = Color.Blue,
            topLeft = Offset(size.width / 2 - 100, size.height - 50), // Centro horizontal y al fondo
            size = Size(200f, 50f) // Dimensiones de la portería
        )
    }
}
