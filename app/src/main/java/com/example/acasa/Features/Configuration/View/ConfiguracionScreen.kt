package com.example.acasa.Features.Configuration.View

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun ConfiguracionScreen(preferences: SharedPreferences) {
    var direccion by remember { mutableStateOf(TextFieldValue(preferences.getString("direccion_casa", "") ?: "")) }

    Column(Modifier.padding(16.dp)) {
        TextField(
            value = direccion,
            onValueChange = { direccion = it },
            label = { Text("Dirección de casa") }
        )
        Button(onClick = { preferences.edit().putString("direccion_casa", direccion.text).apply() }) {
            Text("Guardar Dirección")
        }
    }
}
