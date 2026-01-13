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
import androidx.compose.runtime.remember
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
    val navigationBackStack: NavigationBackStack = remember {
        NavigationBackStack(startDestination = Destination.Weather())
    }

    val bottomTabDestinations: List<Destination> = remember {
        listOf(
            Destination.Weather(),
            Destination.Search,
            Destination.Settings
        )
    }

    BackHandler(enabled = navigationBackStack.entries.size > 1) {
        navigationBackStack.popSafe()
    }

    val currentDestination: Destination = navigationBackStack.entries.last()

    val selectedBottomTabDestination: Destination = when (currentDestination) {
        is Destination.Weather -> Destination.Weather()
        Destination.Search -> Destination.Search
        Destination.Settings -> Destination.Settings
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
                        label = {
                            Text(text = tabLabel(destination = tabDestination))
                        },
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
                        is Destination.Weather -> {
                            NavEntry(
                                key = destination,
                                contentKey = weatherContentKey(destination = destination)
                            ) { entryDestination: Destination ->
                                val weatherDestination: Destination.Weather =
                                    entryDestination as Destination.Weather

                                DetailedWeatherRoute(
                                    latitude = weatherDestination.latitude,
                                    longitude = weatherDestination.longitude
                                )
                            }
                        }

                        Destination.Search -> {
                            NavEntry(
                                key = destination,
                                contentKey = "Destination.Search"
                            ) { _: Destination ->
                                SearchScreen(
                                    onOpenDetails = { latitude: Double, longitude: Double ->
                                        navigationBackStack.replaceCurrent(
                                            destination = Destination.Weather()
                                        )
                                        navigationBackStack.push(
                                            destination = Destination.Weather(
                                                latitude = latitude,
                                                longitude = longitude
                                            )
                                        )
                                    }
                                )
                            }
                        }

                        Destination.Settings -> {
                            NavEntry(
                                key = destination,
                                contentKey = "Destination.Settings"
                            ) { _: Destination ->
                                SettingsView()
                            }
                        }
                    }
                }
            )
        }
    }
}

private fun weatherContentKey(destination: Destination.Weather): String {
    return "Destination.Weather(latitude=${destination.latitude},longitude=${destination.longitude})"
}

private fun tabLabel(destination: Destination): String = when (destination) {
    is Destination.Weather -> "Weather"
    Destination.Search -> "Search"
    Destination.Settings -> "Settings"
}

private fun tabIcon(destination: Destination) = when (destination) {
    is Destination.Weather -> Icons.Default.Home
    Destination.Search -> Icons.Default.Search
    Destination.Settings -> Icons.Default.Settings
}
