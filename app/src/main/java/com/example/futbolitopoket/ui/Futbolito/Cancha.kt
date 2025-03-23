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
    val borderWidth = with(density) { 6.dp.toPx() }
    val goalWidth = 200f
    val goalHeight = 50f
    val penaltyAreaWidth = 300f
    val penaltyAreaHeight = 120f
    val circleRadius = with(density) { 60.dp.toPx() }
    val penaltySpotRadius = with(density) { 5.dp.toPx() }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val fieldWidth = size.width
        val fieldHeight = size.height

        //------bordes del campo
        drawRect(
            color = Color.White,
            topLeft = Offset(borderWidth / 2, borderWidth / 2),
            size = Size(fieldWidth - borderWidth, fieldHeight - borderWidth),
            style = Stroke(width = borderWidth)
        )

        //----------------------------unalLínea central
        drawLine(
            color = Color.White,
            start = Offset(fieldWidth / 2, 0f),
            end = Offset(fieldWidth / 2, fieldHeight),
            strokeWidth = borderWidth,
            cap = StrokeCap.Round
        )

        //-----------un circulo en el centro --------------------------
        drawCircle(
            color = Color.White,
            radius = circleRadius,
            center = Offset(fieldWidth / 2, fieldHeight / 2),
            style = Stroke(width = borderWidth)
        )

        // punto penalti sup
        drawCircle(
            color = Color.White,
            radius = penaltySpotRadius,
            center = Offset(fieldWidth / 2, penaltyAreaHeight + 30f)
        )

        // // punto penalti inf
        drawCircle(
            color = Color.White,
            radius = penaltySpotRadius,
            center = Offset(fieldWidth / 2, fieldHeight - penaltyAreaHeight - 30f)
        )

        // el  penalti  sup
        drawRect(
            color = Color.White,
            topLeft = Offset((fieldWidth - penaltyAreaWidth) / 2, 0f),
            size = Size(penaltyAreaWidth, penaltyAreaHeight),
            style = Stroke(width = borderWidth)
        )

        // el  penalti Infe
        drawRect(
            color = Color.White,
            topLeft = Offset((fieldWidth - penaltyAreaWidth) / 2, fieldHeight - penaltyAreaHeight),
            size = Size(penaltyAreaWidth, penaltyAreaHeight),
            style = Stroke(width = borderWidth)
        )

        // ------------------ Porterias-------------------------------------------------------------
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

        // ---------------------------------la pelota -------------------------------
        drawCircle(
            color = Color.Red,
            radius = with(density) { 20.dp.toPx() },
            center = ballPosition
        )
    }
}
