package com.iiddd.weather.core.ui.systembars

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.iiddd.weather.core.ui.theme.LocalThemeMode
import com.iiddd.weather.core.ui.theme.ThemeMode

@Composable
fun TransparentStatusBarEffect() {
    val view = LocalView.current
    val activity = view.context as? Activity ?: return
    val window = activity.window

    val themeMode: ThemeMode = LocalThemeMode.current
    val isSystemDarkTheme = isSystemInDarkTheme()

    val isDarkThemeEnabled: Boolean = when (themeMode) {
        ThemeMode.System -> isSystemDarkTheme
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }

    // Dark icons when theme is light, white icons when theme is dark
    val useDarkIcons = !isDarkThemeEnabled

    LaunchedEffect(key1 = useDarkIcons) {
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = useDarkIcons
    }
}