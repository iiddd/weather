package com.iiddd.weather.ui.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.iiddd.weather.forecast.presentation.view.DetailedWeatherScreen
import com.iiddd.weather.search.presentation.view.SearchScreen
import com.iiddd.weather.settings.presentation.view.SettingsView

class DetailedWeatherScreenRoute(
    private val lat: Double,
    private val lon: Double,
    private val city: String? = null
) : Screen {
    override val key: String
        get() = "DetailedWeatherRoute($lat,$lon,${city ?: "null"})"

    @Composable
    override fun Content() {
        DetailedWeatherScreen(initialLat = lat, initialLon = lon, initialCity = city)
    }
}

class HomeRootScreen : Screen {
    override val key: String
        get() = "HomeRootScreen"

    @Composable
    override fun Content() {
        DetailedWeatherScreen()
    }
}

class SearchRootScreen(
    private val onOpenDetailsExternal: ((String?, Double, Double) -> Unit)? = null
) : Screen {
    override val key: String
        get() = "SearchRootScreen"

    @Composable
    override fun Content() {
        val innerNav = LocalNavigator.current
        SearchScreen(
            onOpenDetails = { name, lat, lon ->
                if (onOpenDetailsExternal != null) {
                    onOpenDetailsExternal(name, lat, lon)
                } else {
                    innerNav?.push(DetailedWeatherScreenRoute(lat, lon))
                }
            }
        )
    }
}

class SettingsRootScreen : Screen {
    override val key: String
        get() = "SettingsRootScreen"

    @Composable
    override fun Content() {
        SettingsView()
    }
}