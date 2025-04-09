package com.example.acasa.Features.Mapa.View

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.acasa.features.mapa.view.MapaScreen
import org.osmdroid.util.GeoPoint

@Composable
fun MainScreen(context: Context) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "mapa") {
        composable("mapa") {
            MapaScreen(context = context, navController = navController)
        }
        composable("rutasRecientes") {
            RutasRecientesScreen(navController)
        }
        // 👇 Esta es la nueva ruta con coordenadas
        composable(
            route = "mapa?rutaLat={rutaLat}&rutaLon={rutaLon}",
            arguments = listOf(
                navArgument("rutaLat") { nullable = true },
                navArgument("rutaLon") { nullable = true }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("rutaLat")?.toDoubleOrNull()
            val lon = backStackEntry.arguments?.getString("rutaLon")?.toDoubleOrNull()
            val destino = if (lat != null && lon != null) GeoPoint(lat, lon) else null

            MapaScreen(
                context = context,
                navController = navController,
                destinoMarcado = destino
            )
        }
    }
}
