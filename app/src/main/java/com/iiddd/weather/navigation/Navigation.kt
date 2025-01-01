package com.iiddd.weather.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iiddd.weather.ui.weather.view.WeatherView

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.WeatherScreen.route
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