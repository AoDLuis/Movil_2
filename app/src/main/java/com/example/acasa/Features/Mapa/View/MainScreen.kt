package com.example.acasa.Features.Mapa.View

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.acasa.features.mapa.view.MapaScreen
import com.example.acasa.features.mapa.viewmodel.MapaViewModel
import org.osmdroid.util.GeoPoint

@Composable
fun MainScreen(context: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "mapa") {
        composable("mapa") {
            MapaScreen(context = context, navController = navController)
        }
        composable("rutasRecientes") { backStackEntry ->
            // Obtener el entry de la pantalla "mapa"
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("mapa")
            }

            // Compartir el ViewModel entre ambas pantallas
            val viewModel = viewModel<MapaViewModel>(parentEntry)

            RutasRecientesScreen(navController = navController, viewModel = viewModel)
        }

        // ----------------------Esta es la nueva ruta con coordenadas ---------------------------------
        composable(
            route = "mapaScreen/{lat}/{lon}",
            arguments = listOf(
                navArgument("lat") { type = NavType.StringType },
                navArgument("lon") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull() ?: 0.0
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull() ?: 0.0
            val destino = GeoPoint(lat, lon)

            MapaScreen(
                context = context,
                navController = navController,
                destinoMarcado = destino
            )
        }



    }
}
