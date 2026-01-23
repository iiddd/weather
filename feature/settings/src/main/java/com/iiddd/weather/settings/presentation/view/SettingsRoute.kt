package com.iiddd.weather.settings.presentation.view

import androidx.compose.runtime.Composable

@Composable
fun SettingsRoute(
    onNavigateBack: () -> Unit = {}
) {
    SettingsScreen(
        onNavigateBack = onNavigateBack
    )
}