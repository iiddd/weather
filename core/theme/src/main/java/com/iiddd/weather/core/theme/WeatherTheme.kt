package com.iiddd.weather.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun WeatherTheme(
    content: @Composable () -> Unit,
) {
    val themeMode: ThemeMode = LocalThemeMode.current

    val isDarkThemeEnabled: Boolean =
        when (themeMode) {
            ThemeMode.System -> isSystemInDarkTheme()
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
        }

    val colorScheme =
        if (isDarkThemeEnabled) WeatherColorSchemes.dark else WeatherColorSchemes.light

    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content,
    )
}

object WeatherThemeTokens {

    val colors: ColorScheme
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.colorScheme

    val typography: Typography
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography

    val shapes: Shapes
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.shapes

    val dimens: WeatherDimens
        get() = WeatherDimens
}
