package com.iiddd.weather.core.theme
import kotlinx.coroutines.flow.Flow
interface ThemeModeRepository {
    val themeModeFlow: Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)
}
