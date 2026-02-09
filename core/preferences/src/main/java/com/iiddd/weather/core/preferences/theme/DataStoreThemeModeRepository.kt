package com.iiddd.weather.core.preferences.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iiddd.weather.core.theme.ThemeMode
import com.iiddd.weather.core.theme.ThemeModeRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private object ThemePreferencesKeys {
    val themeModeKey = stringPreferencesKey(name = "theme_mode")
}

class DataStoreThemeModeRepository(
    private val dataStore: DataStore<Preferences>,
) : ThemeModeRepository {

    override val themeModeFlow: Flow<ThemeMode> =
        dataStore.data.map { preferences ->
            val storedValue: String? = preferences[ThemePreferencesKeys.themeModeKey]
            storedValue?.toThemeModeOrNull() ?: ThemeMode.System
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[ThemePreferencesKeys.themeModeKey] = themeMode.name
        }
    }
}

private fun String.toThemeModeOrNull(): ThemeMode? =
    ThemeMode.entries.firstOrNull { entry -> entry.name == this }