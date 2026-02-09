package com.iiddd.weather.settings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.core.theme.ThemeMode
import com.iiddd.weather.core.theme.ThemeModeRepository
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val selectedThemeMode: ThemeMode,
)

class SettingsViewModel(
    private val themeModeRepository: ThemeModeRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> =
        themeModeRepository.themeModeFlow
            .map { themeMode ->
                SettingsUiState(selectedThemeMode = themeMode)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = SettingsUiState(selectedThemeMode = ThemeMode.System),
            )

    fun onThemeModeSelected(themeMode: ThemeMode) {
        viewModelScope.launch(context = dispatcherProvider.io) {
            themeModeRepository.setThemeMode(themeMode = themeMode)
        }
    }
}