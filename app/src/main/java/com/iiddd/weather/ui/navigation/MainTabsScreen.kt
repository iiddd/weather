package com.iiddd.weather.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.iiddd.weather.ui.components.TabNavigationItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MainTabsScreen : Screen {
    @Composable
    override fun Content() {
        var homeInnerNav by remember { mutableStateOf<Navigator?>(null) }
        val scope = rememberCoroutineScope()

        val homeTab = remember { HomeTab(onNavigatorReady = { homeInnerNav = it }) }
        val settingsTab = remember { SettingsTab() }

        // Triple<city?, lat, lon>
        val navEvents = remember { MutableSharedFlow<Triple<String?, Double, Double>>(extraBufferCapacity = 1) }

        TabNavigator(homeTab) {
            val tabNavigator = LocalTabNavigator.current

            val searchTab = remember {
                SearchTab(onOpenDetailsExternal = { name, lat, lon ->
                    try { tabNavigator.current = homeTab } catch (_: Exception) {}
                    navEvents.tryEmit(Triple(name, lat, lon))
                })
            }

            LaunchedEffect(navEvents) {
                navEvents.collect { (city, lat, lon) ->
                    delay(150)
                    scope.launch {
                        homeInnerNav?.push(DetailedWeatherScreenRoute(lat, lon, city))
                    }
                }
            }

            Scaffold(
                bottomBar = {
                    NavigationBar {
                        listOf(homeTab, searchTab, settingsTab).forEach { tab ->
                            TabNavigationItem(tab = tab)
                        }
                    }
                },
                content = { paddingValues ->
                    Box(modifier = Modifier.padding(paddingValues)) {
                        CurrentTab()
                    }
                }
            )
        }
    }
}