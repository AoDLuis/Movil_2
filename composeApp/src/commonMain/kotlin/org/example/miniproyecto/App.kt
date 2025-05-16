package org.example.miniproyecto


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.example.miniproyecto.model.WhoaItem
import org.example.miniproyecto.network.ApiService
import org.example.miniproyecto.ui.WhoaCard

@Composable
fun App() {
    MaterialTheme {
        val scope = rememberCoroutineScope()
        var whoaList by remember { mutableStateOf<List<WhoaItem>>(emptyList()) }
        var randomWhoa by remember { mutableStateOf<WhoaItem?>(null) }

        Column(
            modifier = Modifier
                .safeContentPadding()
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Explora los 'Whoa!' de Keanu Reeves!", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                scope.launch {
                    whoaList = ApiService.getRandomWhoas(5)
                    randomWhoa = null
                }
            }) {
                Text("Mostrar películas aleatorias")
            }


            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                scope.launch {
                    randomWhoa = ApiService.getRandomWhoa()
                    whoaList = emptyList()
                }
            }) {
                Text("Mostrar Whoa aleatorio")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (whoaList.isNotEmpty()) {
                LazyColumn {
                    items(whoaList) { item ->
                        WhoaCard(item)
                    }
                }
            }

            randomWhoa?.let {
                WhoaCard(it)
            }
        }
    }
}
