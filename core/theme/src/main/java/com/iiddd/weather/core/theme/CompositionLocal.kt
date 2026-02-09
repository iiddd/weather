package com.iiddd.weather.core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf

val LocalThemeMode: ProvidableCompositionLocal<ThemeMode> =
    staticCompositionLocalOf { ThemeMode.System }

@Composable
fun ProvideThemeMode(
    themeMode: ThemeMode,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalThemeMode provides themeMode,
    ) {
        content()
    }
}

