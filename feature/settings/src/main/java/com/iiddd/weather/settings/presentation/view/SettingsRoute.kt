package com.iiddd.weather.settings.presentation.view

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iiddd.weather.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(
    onNavigateBack: () -> Unit = {},
    settingsViewModel: SettingsViewModel = koinViewModel(),
) {
    val settingsUiState = settingsViewModel
        .settingsUiState
        .collectAsStateWithLifecycle()

    SettingsScreen(
        title = "Settings",
        selectedThemeMode = settingsUiState.value.selectedThemeMode,
        onThemeModeSelected = { themeMode ->
            settingsViewModel.onThemeModeSelected(themeMode = themeMode)
        },
        onNavigateBack = onNavigateBack,
    )
}