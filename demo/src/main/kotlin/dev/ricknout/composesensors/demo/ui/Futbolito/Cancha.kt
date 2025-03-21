package dev.ricknout.composesensors.demo.ui.Futbolito

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun Cancha(
    ballPosition: Offset
) {
    val density = LocalDensity.current
    val borderWidth = with(density) { 6.dp.toPx() } // Grosor de las líneas
    val goalWidth = 200f
    val goalHeight = 50f
    val penaltyAreaWidth = 300f
    val penaltyAreaHeight = 120f
    val bottomMargin = 20f // 🔹 Ajuste para evitar que el fondo quede muy bajo
    val circleRadius = with(density) { 60.dp.toPx() }
    val penaltySpotRadius = with(density) { 5.dp.toPx() }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val fieldWidth = size.width
        val fieldHeight = size.height - bottomMargin // 🔹 Ajustar el tamaño de la cancha

        // ⚪ Dibujar bordes del campo
        drawRect(
            color = Color.White,
            topLeft = Offset(borderWidth / 2, borderWidth / 2),
            size = Size(fieldWidth - borderWidth, fieldHeight - borderWidth),
            style = Stroke(width = borderWidth)
        )

        // ⚪ Línea central
        drawLine(
            color = Color.White,
            start = Offset(fieldWidth / 2, 0f),
            end = Offset(fieldWidth / 2, fieldHeight),
            strokeWidth = borderWidth,
            cap = StrokeCap.Round
        )

        // ⚪ Círculo central
        drawCircle(
            color = Color.White,
            radius = circleRadius,
            center = Offset(fieldWidth / 2, fieldHeight / 2),
            style = Stroke(width = borderWidth)
        )

        // ⚪ Punto de penalti (Superior)
        drawCircle(
            color = Color.White,
            radius = penaltySpotRadius,
            center = Offset(fieldWidth / 2, penaltyAreaHeight + 30f)
        )

        // ⚪ Punto de penalti (Inferior)
        drawCircle(
            color = Color.White,
            radius = penaltySpotRadius,
            center = Offset(fieldWidth / 2, fieldHeight - penaltyAreaHeight - 30f)
        )

        // 🥅 Área de penalti (Superior)
        drawRect(
            color = Color.White,
            topLeft = Offset((fieldWidth - penaltyAreaWidth) / 2, 0f),
            size = Size(penaltyAreaWidth, penaltyAreaHeight),
            style = Stroke(width = borderWidth)
        )

        // 🥅 Área de penalti (Inferior)
        drawRect(
            color = Color.White,
            topLeft = Offset((fieldWidth - penaltyAreaWidth) / 2, fieldHeight - penaltyAreaHeight),
            size = Size(penaltyAreaWidth, penaltyAreaHeight),
            style = Stroke(width = borderWidth)
        )

        // 🥅 Porterías
        drawRect(
            color = Color.White,
            topLeft = Offset((fieldWidth - goalWidth) / 2, 0f),
            size = Size(goalWidth, goalHeight),
            style = Stroke(width = borderWidth)
        )

        drawRect(
            color = Color.White,
            topLeft = Offset((fieldWidth - goalWidth) / 2, fieldHeight - goalHeight),
            size = Size(goalWidth, goalHeight),
            style = Stroke(width = borderWidth)
        )

        // 🟠 Dibujar pelota
        drawCircle(
            color = Color.Red,
            radius = with(density) { 20.dp.toPx() },
            center = ballPosition
        )
    }
}
