package com.iiddd.weather.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.iiddd.weather.R as AppR
import com.iiddd.weather.core.ui.systembars.WeatherWindowInsets
import com.iiddd.weather.favorites.presentation.view.FavoritesRoute
import com.iiddd.weather.forecast.presentation.view.DetailedWeatherRoute
import com.iiddd.weather.search.presentation.view.SearchRoute
import com.iiddd.weather.settings.presentation.view.SettingsRoute
import com.iiddd.weather.ui.navigation.Destination
import com.iiddd.weather.ui.navigation.NavigationBackStack
import com.iiddd.weather.ui.navigation.WeatherNavigationState
import com.iiddd.weather.ui.navigation.popSafe
import com.iiddd.weather.ui.navigation.rememberNavigationBackStack
import com.iiddd.weather.ui.navigation.rememberWeatherNavigationState

@Composable
fun NavigationHost() {
    val navigationBackStack: NavigationBackStack = rememberNavigationBackStack(
        startDestination = Destination.Weather()
    )

    val weatherNavigationState: WeatherNavigationState = rememberWeatherNavigationState()

    val bottomTabDestinations: List<Destination> = remember {
        listOf(
            Destination.Weather(),
            Destination.Favorites,
            Destination.Search,
            Destination.Settings,
        )
    }

    BackHandler(enabled = navigationBackStack.entries.size > 1) {
        navigationBackStack.popSafe()
    }

    val currentDestination: Destination = navigationBackStack.entries.last()

    val selectedBottomTabDestination: Destination = when (currentDestination) {
        is Destination.Weather -> Destination.Weather()
        Destination.Favorites -> Destination.Favorites
        Destination.Search -> Destination.Search
        Destination.Settings -> Destination.Settings
    }

    Scaffold(
        contentWindowInsets = WeatherWindowInsets.None,
        bottomBar = {
            MainBottomNavigationBar(
                bottomTabDestinations = bottomTabDestinations,
                selectedBottomTabDestination = selectedBottomTabDestination,
                onTabSelected = { tabDestination: Destination ->
                    when (tabDestination) {
                        is Destination.Weather -> {
                            navigationBackStack.replaceCurrent(
                                destination = weatherNavigationState.currentWeatherDestination
                            )
                        }
                        else -> {
                            navigationBackStack.replaceCurrent(destination = tabDestination)
                        }
                    }
                },
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            MainNavDisplay(
                navigationBackStack = navigationBackStack,
                weatherNavigationState = weatherNavigationState,
            )
        }
    }
}

@Composable
private fun MainBottomNavigationBar(
    bottomTabDestinations: List<Destination>,
    selectedBottomTabDestination: Destination,
    onTabSelected: (Destination) -> Unit,
) {
    NavigationBar {
        bottomTabDestinations.forEach { tabDestination: Destination ->
            NavigationBarItem(
                selected = selectedBottomTabDestination::class == tabDestination::class,
                onClick = { onTabSelected(tabDestination) },
                label = { Text(text = tabLabel(destination = tabDestination)) },
                icon = {
                    Icon(
                        imageVector = tabIcon(destination = tabDestination),
                        contentDescription = tabLabel(destination = tabDestination),
                    )
                },
            )
        }
    }
}

@Composable
private fun MainNavDisplay(
    navigationBackStack: NavigationBackStack,
    weatherNavigationState: WeatherNavigationState,
) {
    NavDisplay(
        backStack = navigationBackStack.entries,
        onBack = { navigationBackStack.popSafe() },
        entryProvider = { destination: Destination ->
            when (destination) {
                is Destination.Weather -> {
                    NavEntry(
                        key = destination,
                        contentKey = destination.toString(),
                    ) { entryDestination: Destination ->
                        val weatherDestination: Destination.Weather =
                            entryDestination as Destination.Weather

                        weatherNavigationState.updateWeatherDestination(destination = weatherDestination)

                        DetailedWeatherRoute(
                            latitude = weatherDestination.latitude,
                            longitude = weatherDestination.longitude,
                            useDeviceLocation = weatherDestination.useDeviceLocation,
                        )
                    }
                }

                Destination.Search -> {
                    NavEntry(
                        key = destination,
                        contentKey = destination.toString(),
                    ) { _: Destination ->
                        SearchRoute(
                            onOpenDetails = { latitude: Double, longitude: Double ->
                                val newWeatherDestination = Destination.Weather(
                                    latitude = latitude,
                                    longitude = longitude,
                                    useDeviceLocation = false,
                                )
                                weatherNavigationState.updateWeatherDestination(
                                    destination = newWeatherDestination
                                )
                                navigationBackStack.replaceCurrent(
                                    destination = newWeatherDestination
                                )
                            },
                        )
                    }
                }

                Destination.Settings -> {
                    NavEntry(
                        key = destination,
                        contentKey = destination.toString(),
                    ) { _: Destination ->
                        SettingsRoute()
                    }
                }

                Destination.Favorites -> {
                    NavEntry(
                        key = destination,
                        contentKey = destination.toString(),
                    ) { _: Destination ->
                        FavoritesRoute(
                            onOpenDetails = { latitude: Double, longitude: Double ->
                                val newWeatherDestination = Destination.Weather(
                                    latitude = latitude,
                                    longitude = longitude,
                                    useDeviceLocation = false,
                                )
                                weatherNavigationState.updateWeatherDestination(
                                    destination = newWeatherDestination
                                )
                                navigationBackStack.replaceCurrent(
                                    destination = newWeatherDestination
                                )
                            },
                        )
                    }
                }
            }
        },
    )
}

@Composable
private fun tabLabel(destination: Destination): String = when (destination) {
    is Destination.Weather -> stringResource(AppR.string.home_tab_label)
    Destination.Favorites -> stringResource(AppR.string.favorites_tab_label)
    Destination.Search -> stringResource(AppR.string.search_tab_label)
    Destination.Settings -> stringResource(AppR.string.settings_tab_label)
}

private fun tabIcon(destination: Destination): ImageVector = when (destination) {
    is Destination.Weather -> Icons.Default.Home
    Destination.Favorites -> Icons.Default.Favorite
    Destination.Search -> Icons.Default.Search
    Destination.Settings -> Icons.Default.Settings
}