//package org.example.miniproyecto

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.miniproyecto.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Whoa",
    ) {
        App()
    }
}