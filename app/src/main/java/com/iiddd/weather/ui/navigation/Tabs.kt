package com.iiddd.weather.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.iiddd.weather.search.presentation.view.SearchView
import com.iiddd.weather.settings.presentation.view.SettingsView
import com.iiddd.weather.home.presentation.view.HomeScreen

class HomeTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 0u,
            title = "Home",
            icon = rememberVectorPainter(Icons.Default.Home)
        )

    @Composable
    override fun Content() {
        HomeScreen()
    }
}

class SearchTab : Tab {
    override val options: TabOptions
        @Composable
        get() = TabOptions(
            index = 1u,
            title = "Search",
            icon = rememberVectorPainter(Icons.Default.Search)
        )

    @Composable
    override fun Content() {
        SearchView()
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
        SettingsView()
    }
}