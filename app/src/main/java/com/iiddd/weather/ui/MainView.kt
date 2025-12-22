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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import com.iiddd.weather.forecast.presentation.view.DetailedWeatherRoute
import com.iiddd.weather.search.presentation.view.SearchScreen
import com.iiddd.weather.settings.presentation.view.SettingsView
import com.iiddd.weather.ui.navigation.Destination
import com.iiddd.weather.ui.navigation.NavigationBackStack
import com.iiddd.weather.ui.navigation.popSafe

@Composable
fun MainView() {
    val backStack = remember { NavigationBackStack(Destination.Home) }
    val tabs = remember { listOf(Destination.Home, Destination.Search, Destination.Settings) }
    var selectedTab by remember { mutableStateOf<Destination>(Destination.Home) }

    BackHandler(enabled = backStack.entries.size > 1) {
        backStack.popSafe()
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = selectedTab::class == tab::class,
                        onClick = {
                            selectedTab = tab
                            backStack.replaceCurrent(tab)
                        },
                        label = { Text(tabLabel(tab)) },
                        icon = { Icon(tabIcon(tab), contentDescription = tabLabel(tab)) }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            NavDisplay(
                backStack = backStack.entries,
                onBack = { backStack.popSafe() },
                entryProvider = { destination: Destination ->
                    when (destination) {
                        Destination.Home -> NavEntry(destination) { DetailedWeatherRoute() }

                        Destination.Search -> NavEntry(destination) {
                            SearchScreen(
                                onOpenDetails = { lat, lon ->
                                    backStack.push(Destination.Details(lat, lon))
                                }
                            )
                        }

                        Destination.Settings -> NavEntry(destination) { SettingsView() }

                        is Destination.Details -> NavEntry(destination) {
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