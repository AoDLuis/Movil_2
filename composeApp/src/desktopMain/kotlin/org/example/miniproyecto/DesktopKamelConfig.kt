package org.example.miniproyecto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher

// src/desktopMain/kotlin/DesktopKamelConfig.kt

@Composable
fun DesktopKamelProvider(content: @Composable () -> Unit) {
    val desktopConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher()  // Carga recursos desde src/desktopMain/resources
    }
    CompositionLocalProvider(LocalKamelConfig provides desktopConfig) {
        content()
    }
}
