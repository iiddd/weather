package com.iiddd.weather.core.ui.theme

import kotlinx.coroutines.flow.Flow

interface ThemeModeRepository {
    val themeModeFlow: Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)
}