package com.example.broadcastreceiver_telefona.ui.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.broadcastreceiver_telefona.ViewModel.AutoReplyViewModel


@Composable
fun MainScreen(viewModel: AutoReplyViewModel = hiltViewModel()) {
    val number by viewModel.number.collectAsState()
    val message by viewModel.message.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = number,
            onValueChange = { newValue -> viewModel.saveConfig(newValue, message) },
            label = { Text("Numero de telefono") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = message,
            onValueChange = { newValue -> viewModel.saveConfig(number, newValue) },
            label = { Text("Mensaje automatico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (number.isNotEmpty() && message.isNotEmpty()) {
                    viewModel.saveConfig(number, message)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}
