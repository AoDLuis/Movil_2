package org.example.miniproyecto.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.example.miniproyecto.model.WhoaItem
import org.example.miniproyecto.network.loadImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import io.kamel.core.Resource

@Composable
fun WhoaCard(item: WhoaItem) {
    val posterUrl = item.poster?.trim().orEmpty()

    if (posterUrl.isBlank() || !posterUrl.startsWith("http")) {
        Text("❌ URL de imagen no válida", color = MaterialTheme.colorScheme.error)
        println("URL inválida: '$posterUrl'")
        return
    }

    val image = loadImage(posterUrl)

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎬 ${item.movie} (${item.year})", style = MaterialTheme.typography.titleMedium)
            Text("👤 ${item.character} - Dirigido por ${item.director}", style = MaterialTheme.typography.bodyMedium)
            Text("🕒 ${item.timestamp} / ${item.movie_duration}", style = MaterialTheme.typography.bodySmall)
            Text("🗨️ \"${item.full_line}\"", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(8.dp))

            when (val result = image) {
                is Resource.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .height(180.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is Resource.Success -> {
                    Image(
                        painter = result.value,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        contentScale = ContentScale.Fit
                    )
                    println("✅ Imagen cargada correctamente desde: $posterUrl")
                }

                is Resource.Failure -> {
                    Text("❌ Error al cargar imagen", color = MaterialTheme.colorScheme.error)
                    println("❌ Error al cargar imagen desde $posterUrl: ${result.exception}")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
