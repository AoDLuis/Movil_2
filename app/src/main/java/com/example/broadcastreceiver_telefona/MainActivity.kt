package com.example.broadcastreceiver_telefona

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import android.Manifest


class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val allGranted = results.all { it.value }
            if (!allGranted) {
                Toast.makeText(this, "Se requieren permisos para el funcionamiento", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestPermissions()
        super.onCreate(savedInstanceState)
        setContent {
            AutoReplyApp(this)
        }
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.RECEIVE_SMS
        )

        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

}

@Composable
fun AutoReplyApp(context: Context) {
    val sharedPreferences = context.getSharedPreferences("AutoReplyPrefs", Context.MODE_PRIVATE)

    var number by remember { mutableStateOf(sharedPreferences.getString("savedNumber", "") ?: "") }
    var message by remember { mutableStateOf(sharedPreferences.getString("savedMessage", "") ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = number,
            onValueChange = { number = it },
            label = { Text("Numero de telefono") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje automatico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (number.isNotEmpty() && message.isNotEmpty()) {
                    sharedPreferences.edit().apply {
                        putString("savedNumber", number)
                        putString("savedMessage", message)
                        apply()
                    }
                    Toast.makeText(context, "Configuracion guardada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Ingrese un numero y mensaje", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }





}
