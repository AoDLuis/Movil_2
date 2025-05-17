package org.example.miniproyecto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig

// src/jsMain/kotlin/WebKamelConfig.kt

@Composable
fun WebKamelProvider(content: @Composable () -> Unit) {
    val webConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        // Configuraciones específicas web si se requieren
    }
    CompositionLocalProvider(LocalKamelConfig provides webConfig) {
        content()
    }
}
