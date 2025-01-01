package com.iiddd.weather.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iiddd.weather.components.BottomNavigationBar
import com.iiddd.weather.navigation.Screen.SearchScreen
import com.iiddd.weather.ui.weather.view.WeatherView

@Composable
fun Navigation() {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.WeatherScreen.route,
            Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.WeatherScreen.route) {
                WeatherView()
            }
            composable(route = Screen.SearchScreen.route) {
                //TODO: Implement Finder screen
            }
            composable(route = Screen.SettingsScreen.route) {
                //TODO: Implement Settings screen
            }
        }
    }
//    NavHost(
//        navController = navController,
//        startDestination = Screen.WeatherScreen.route
//    ) {
//        composable(route = Screen.WeatherScreen.route) {
//            WeatherView()
//        }
//        composable(route = Screen.SearchScreen.route) {
//            //TODO: Implement Finder screen
//        }
//        composable(route = Screen.SettingsScreen.route) {
//            //TODO: Implement Settings screen
//        }
//    }
}