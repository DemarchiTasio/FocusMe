package com.android.app.focusmate

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent() {
            MyApp {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "calendar"
                    ){
                        composable(AppRoutes.CALENDAR) { CalendarScreen(navController = navController) }
                        composable(AppRoutes.HABITS) { HabitScreen(navController = navController) }
                        composable(AppRoutes.MINDFULNESS) { MindfulnessScreen(navController = navController) }
                        // Agregar más destinos para las nuevas pantallas
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        AppRoutes.CALENDAR,
        AppRoutes.HABITS,
        AppRoutes.MINDFULNESS
        // Agregar más rutas para las nuevas pantallas
    )
    BottomNavigation {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { route ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null
                    )
                }, // Cambiar iconos según la ruta
                label = { Text(route) }, // Puedes tener etiquetas personalizadas para cada ruta
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}