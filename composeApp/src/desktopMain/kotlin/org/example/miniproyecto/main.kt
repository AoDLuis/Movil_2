//package org.example.miniproyecto

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.example.miniproyecto.App
import org.example.miniproyecto.DesktopKamelProvider

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Whoa",
    ) {
        DesktopKamelProvider {
            App()
        }
    }
}