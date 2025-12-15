package com.iiddd.weather.ui.navigation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.forecast.presentation.view.DetailedWeatherScreen
import com.iiddd.weather.search.presentation.view.SearchScreen
import com.iiddd.weather.settings.presentation.view.SettingsView

@Composable
private fun ThemedContent(content: @Composable () -> Unit) {
    WeatherTheme(darkTheme = isSystemInDarkTheme()) {
        content()
    }
}

class DetailedWeatherScreenRoute(
    private val latitude: Double,
    private val longitude: Double
) : Screen {
    override val key: String
        get() = "DetailedWeatherRoute($latitude,$longitude)"

    @Composable
    override fun Content() {
        ThemedContent {
            DetailedWeatherScreen(
                initialLatitude = latitude,
                initialLongitude = longitude
            )
        }
    }
}

class HomeRootScreen : Screen {
    override val key: String
        get() = "HomeRootScreen"

    @Composable
    override fun Content() {
        ThemedContent {
            DetailedWeatherScreen()
        }
    }
}

class SearchRootScreen(
    private val onOpenDetailsExternal: ((Double, Double) -> Unit)? = null
) : Screen {
    override val key: String
        get() = "SearchRootScreen"

    @Composable
    override fun Content() {
        val innerNav = LocalNavigator.current
        ThemedContent {
            SearchScreen(
                onOpenDetails = { lat, lon ->
                    if (onOpenDetailsExternal != null) {
                        onOpenDetailsExternal(lat, lon)
                    } else {
                        innerNav?.push(DetailedWeatherScreenRoute(lat, lon))
                    }
                }
            )
        }
    }
}

class SettingsRootScreen : Screen {
    override val key: String
        get() = "SettingsRootScreen"

    @Composable
    override fun Content() {
        ThemedContent {
            SettingsView()
        }
    }
}