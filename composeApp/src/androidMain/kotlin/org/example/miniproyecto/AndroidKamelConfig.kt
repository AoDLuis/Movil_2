package org.example.miniproyecto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher
import io.kamel.image.config.resourcesIdMapper
import io.ktor.client.plugins.logging.LoggingFormat


@Composable
fun AndroidKamelProvider(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val androidConfig = KamelConfig {
        takeFrom(KamelConfig.Default)
        resourcesFetcher(context)
        resourcesIdMapper(context)
    }
    CompositionLocalProvider(LocalKamelConfig provides androidConfig) {
        content()
    }
}
