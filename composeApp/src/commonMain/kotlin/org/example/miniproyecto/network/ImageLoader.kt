package org.example.miniproyecto.network

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import io.kamel.core.Resource
import io.kamel.image.asyncPainterResource


@Composable
fun loadImage(url: String): Resource<Painter> {
    return asyncPainterResource(url)
}