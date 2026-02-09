package com.iiddd.weather.core.preferences.theme

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.iiddd.weather.core.theme.ThemeMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreThemeModeRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @TempDir
    lateinit var tempDir: File

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var themeModeRepository: DataStoreThemeModeRepository

    private val themeModeKey = stringPreferencesKey(name = "theme_mode")

    @BeforeEach
    fun setUp() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
        ) {
            File(tempDir, "test_theme.preferences_pb")
        }
        themeModeRepository = DataStoreThemeModeRepository(dataStore = dataStore)
    }

    @Test
    fun `themeModeFlow returns System when preferences is empty`() = testScope.runTest {
        val result = themeModeRepository.themeModeFlow.first()

        assertEquals(ThemeMode.System, result)
    }

    @Test
    fun `themeModeFlow returns Light when preferences contains Light`() = testScope.runTest {
        dataStore.edit { preferences ->
            preferences[themeModeKey] = "Light"
        }

        val result = themeModeRepository.themeModeFlow.first()

        assertEquals(ThemeMode.Light, result)
    }

    @Test
    fun `themeModeFlow returns Dark when preferences contains Dark`() = testScope.runTest {
        dataStore.edit { preferences ->
            preferences[themeModeKey] = "Dark"
        }

        val result = themeModeRepository.themeModeFlow.first()

        assertEquals(ThemeMode.Dark, result)
    }

    @Test
    fun `themeModeFlow returns System when preferences contains invalid value`() = testScope.runTest {
        dataStore.edit { preferences ->
            preferences[themeModeKey] = "InvalidValue"
        }

        val result = themeModeRepository.themeModeFlow.first()

        assertEquals(ThemeMode.System, result)
    }

    @Test
    fun `themeModeFlow returns System when preferences contains System`() = testScope.runTest {
        dataStore.edit { preferences ->
            preferences[themeModeKey] = "System"
        }

        val result = themeModeRepository.themeModeFlow.first()

        assertEquals(ThemeMode.System, result)
    }

    @Test
    fun `setThemeMode stores Light correctly`() = testScope.runTest {
        themeModeRepository.setThemeMode(themeMode = ThemeMode.Light)

        val result = themeModeRepository.themeModeFlow.first()

        assertEquals(ThemeMode.Light, result)
    }

    @Test
    fun `setThemeMode stores Dark correctly`() = testScope.runTest {
        themeModeRepository.setThemeMode(themeMode = ThemeMode.Dark)

        val result = themeModeRepository.themeModeFlow.first()

        assertEquals(ThemeMode.Dark, result)
    }

    @Test
    fun `setThemeMode stores System correctly`() = testScope.runTest {
        themeModeRepository.setThemeMode(themeMode = ThemeMode.System)

        val result = themeModeRepository.themeModeFlow.first()

        assertEquals(ThemeMode.System, result)
    }
}
