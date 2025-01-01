package com.iiddd.weather.navigation

sealed class Screen(val route: String) {
    data object WeatherScreen : Screen("weather_screen")
    data object SearchScreen : Screen("search_screen")
    data object SettingsScreen: Screen("settings_screen")
}