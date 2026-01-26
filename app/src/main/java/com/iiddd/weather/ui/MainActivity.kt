package com.iiddd.weather.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iiddd.weather.core.ui.systembars.TransparentStatusBarEffect
import com.iiddd.weather.core.ui.theme.ProvideThemeMode
import com.iiddd.weather.core.ui.theme.ThemeMode
import com.iiddd.weather.core.ui.theme.ThemeModeRepository
import com.iiddd.weather.core.ui.theme.WeatherTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val themeModeRepository: ThemeModeRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val themeMode: ThemeMode =
                themeModeRepository.themeModeFlow.collectAsStateWithLifecycle(
                    initialValue = ThemeMode.System,
                ).value

            ProvideThemeMode(
                themeMode = themeMode,
            ) {
                WeatherTheme {
                    TransparentStatusBarEffect()
                    MainView()
                }
            }
        }
    }
}