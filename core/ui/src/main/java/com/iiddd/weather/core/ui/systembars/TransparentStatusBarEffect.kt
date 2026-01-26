package com.iiddd.weather.core.ui.systembars

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun TransparentStatusBarEffect(
    darkIcons: Boolean = !isSystemInDarkTheme(),
) {
    val view = LocalView.current
    val activity = view.context as? Activity ?: return
    val window = activity.window

    DisposableEffect(darkIcons) {
        val controller = WindowCompat.getInsetsController(window, view)
        val previousColor = window.statusBarColor
        val previousIcons = controller.isAppearanceLightStatusBars

        window.statusBarColor = Color.Transparent.toArgb()
        controller.isAppearanceLightStatusBars = darkIcons

        onDispose {
            window.statusBarColor = previousColor
            controller.isAppearanceLightStatusBars = previousIcons
        }
    }
}