package com.iiddd.weather.settings.presentation.view

import androidx.compose.runtime.Composable
import com.iiddd.weather.core.ui.theme.ThemeMode

@Composable
fun SettingsScreen(
    title: String,
    selectedThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onNavigateBack: () -> Unit,
) {
    SettingsScreenContent(
        title = title,
        selectedThemeMode = selectedThemeMode,
        onThemeModeSelected = onThemeModeSelected,
        onNavigateBack = onNavigateBack,
    )
}