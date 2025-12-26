package com.iiddd.weather.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.iiddd.weather.forecast.presentation.view.DetailedWeatherRoute
import com.iiddd.weather.search.presentation.view.SearchScreen
import com.iiddd.weather.settings.presentation.view.SettingsView
import com.iiddd.weather.ui.navigation.Destination
import com.iiddd.weather.ui.navigation.NavigationBackStack
import com.iiddd.weather.ui.navigation.popSafe

@Composable
fun MainView() {
    val navigationBackStack = androidx.compose.runtime.remember {
        NavigationBackStack(startDestination = Destination.Home)
    }

    val bottomTabDestinations = androidx.compose.runtime.remember {
        listOf(
            Destination.Home,
            Destination.Search,
            Destination.Settings
        )
    }

    BackHandler(enabled = navigationBackStack.entries.size > 1) {
        navigationBackStack.popSafe()
    }

    val currentDestination: Destination = navigationBackStack.entries.last()

    val selectedBottomTabDestination: Destination = when (currentDestination) {
        Destination.Home -> Destination.Home
        Destination.Search -> Destination.Search
        Destination.Settings -> Destination.Settings
        is Destination.Details -> Destination.Home
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomTabDestinations.forEach { tabDestination: Destination ->
                    NavigationBarItem(
                        selected = selectedBottomTabDestination::class == tabDestination::class,
                        onClick = {
                            navigationBackStack.replaceCurrent(destination = tabDestination)
                        },
                        label = { Text(text = tabLabel(destination = tabDestination)) },
                        icon = {
                            Icon(
                                imageVector = tabIcon(destination = tabDestination),
                                contentDescription = tabLabel(destination = tabDestination)
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavDisplay(
                backStack = navigationBackStack.entries,
                onBack = {
                    navigationBackStack.popSafe()
                },
                entryProvider = { destination: Destination ->
                    when (destination) {
                        Destination.Home -> NavEntry(key = destination) {
                            DetailedWeatherRoute()
                        }

                        Destination.Search -> NavEntry(key = destination) {
                            SearchScreen(
                                onOpenDetails = { latitude: Double, longitude: Double ->
                                    navigationBackStack.replaceCurrent(destination = Destination.Home)
                                    navigationBackStack.push(
                                        destination = Destination.Details(
                                            latitude = latitude,
                                            longitude = longitude
                                        )
                                    )
                                }
                            )
                        }

                        Destination.Settings -> NavEntry(key = destination) {
                            SettingsView()
                        }

                        is Destination.Details -> NavEntry(key = destination) {
                            DetailedWeatherRoute(
                                initialLatitude = destination.latitude,
                                initialLongitude = destination.longitude
                            )
                        }
                    }
                }
            )
        }
    }
}

private fun tabLabel(destination: Destination): String = when (destination) {
    Destination.Home -> "Home"
    Destination.Search -> "Search"
    Destination.Settings -> "Settings"
    is Destination.Details -> "Details"
}

private fun tabIcon(destination: Destination) = when (destination) {
    Destination.Home, is Destination.Details -> Icons.Default.Home
    Destination.Search -> Icons.Default.Search
    Destination.Settings -> Icons.Default.Settings
}