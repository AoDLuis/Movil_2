package com.example.acasa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.acasa.Features.Mapa.View.MainScreen
import com.example.acasa.features.mapa.view.MapaScreen
import com.example.acasa.ui.theme.ACasaTheme
import com.example.acasa.utils.RequestLocationPermission

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels() // ViewModel para manejar el estado

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ACasaTheme {
                val hasPermission by viewModel.hasLocationPermission.collectAsState()

                RequestLocationPermission(
                    context = this@MainActivity,
                    onPermissionGranted = { viewModel.setPermissionGranted(true) },
                    onPermissionDenied = { viewModel.setPermissionGranted(false) }
                )

                if (hasPermission) {
                    MainScreen(context = this)
                }
            }
        }
    }
}
