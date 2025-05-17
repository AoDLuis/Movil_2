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
import org.example.miniproyecto.ui.MiniProyectoTheme
import org.example.miniproyecto.ui.WhoaCard


@Composable
fun App() {
    MiniProyectoTheme {
        val scope = rememberCoroutineScope()
        var whoaList by remember { mutableStateOf<List<WhoaItem>>(emptyList()) }
        var randomWhoa by remember { mutableStateOf<WhoaItem?>(null) }

        Scaffold(
            topBar = {
                Text(
                    text = "¡Los 'Whoa!' de Keanu Reeves!",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(16.dp)
                        .widthIn(max = 600.dp)
                        .safeContentPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "¡Explora los 'Whoa!' de Keanu Reeves!",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                scope.launch {
                                    whoaList = ApiService.getRandomWhoas(5)
                                    randomWhoa = null
                                }
                            }
                        ) {
                            Text("Películas aleatorias")
                        }

                        ElevatedButton(
                            onClick = {
                                scope.launch {
                                    randomWhoa = ApiService.getRandomWhoa()
                                    whoaList = emptyList()
                                }
                            }
                        ) {
                            Text("Whoa aleatorio")
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        if (whoaList.isNotEmpty()) {
                            items(whoaList) { item ->
                                WhoaCard(item)
                            }
                        }

                        randomWhoa?.let {
                            item {
                                WhoaCard(it)
                            }
                        }
                    }

                }
            }
        )
    }
}
