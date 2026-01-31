package com.iiddd.weather.core.ui.systembars

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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

    DisposableEffect(key1 = useDarkIcons) {
        val controller = WindowCompat.getInsetsController(window, view)
        val previousColor = window.statusBarColor
        val previousIcons = controller.isAppearanceLightStatusBars

        window.statusBarColor = Color.Transparent.toArgb()
        controller.isAppearanceLightStatusBars = useDarkIcons

        onDispose {
            window.statusBarColor = previousColor
            controller.isAppearanceLightStatusBars = previousIcons
        }
    }
}