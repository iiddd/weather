package com.iiddd.weather.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions

class HomeTab(
    private val onNavigatorReady: ((Navigator?) -> Unit)? = null
) : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "Home",
            icon = rememberVectorPainter(Icons.Default.Home)
        )

    @Composable
    override fun Content() {
        Navigator(HomeRootScreen()) {
            val innerNav = LocalNavigator.current
            LaunchedEffect(innerNav) {
                onNavigatorReady?.invoke(innerNav)
            }
            CurrentScreen()
        }
    }
}

class SearchTab(
    private val onOpenDetailsExternal: ((Double, Double) -> Unit)? = null
) : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 1u,
            title = "Search",
            icon = rememberVectorPainter(Icons.Default.Search)
        )

    @Composable
    override fun Content() {
        Navigator(SearchRootScreen(onOpenDetailsExternal)) {
            CurrentScreen()
        }
    }
}

class SettingsTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 2u,
            title = "Settings",
            icon = rememberVectorPainter(Icons.Default.Settings)
        )

    @Composable
    override fun Content() {
        Navigator(SettingsRootScreen()) {
            CurrentScreen()
        }
    }
}