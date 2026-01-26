package com.iiddd.weather.settings.presentation.viewmodel

import com.iiddd.weather.core.testutils.UnitTestDispatcherProvider
import com.iiddd.weather.core.ui.theme.ThemeMode
import com.iiddd.weather.core.ui.theme.ThemeModeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

private class FakeThemeModeRepository(
    initialThemeMode: ThemeMode = ThemeMode.System,
) : ThemeModeRepository {

    private val mutableThemeModeStateFlow: MutableStateFlow<ThemeMode> =
        MutableStateFlow(value = initialThemeMode)

    override val themeModeFlow: Flow<ThemeMode> = mutableThemeModeStateFlow.asStateFlow()

    private var mutableLastSetThemeMode: ThemeMode? = null

    val lastSetThemeMode: ThemeMode?
        get() = mutableLastSetThemeMode

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        mutableLastSetThemeMode = themeMode
        mutableThemeModeStateFlow.value = themeMode
    }

    fun emitThemeMode(themeMode: ThemeMode) {
        mutableThemeModeStateFlow.value = themeMode
    }

    fun currentThemeMode(): ThemeMode = mutableThemeModeStateFlow.value
}

class SettingsViewModelTest {

    private val dispatcherProvider: UnitTestDispatcherProvider = UnitTestDispatcherProvider()

    private lateinit var themeModeRepository: FakeThemeModeRepository
    private lateinit var settingsViewModel: SettingsViewModel

    @BeforeEach
    fun setUp() {
        themeModeRepository = FakeThemeModeRepository(initialThemeMode = ThemeMode.System)

        settingsViewModel =
            SettingsViewModel(
                themeModeRepository = themeModeRepository,
                dispatcherProvider = dispatcherProvider,
            )
    }

    @Test
    fun `settingsUiState initialValue reflects repository themeModeFlow`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val expectedThemeMode = themeModeRepository.currentThemeMode()

            val actualThemeMode = settingsViewModel.settingsUiState.value.selectedThemeMode

            assertEquals(expectedThemeMode, actualThemeMode)
        }

    @Test
    fun `settingsUiState updates when repository emits new theme mode`() =
        runTest(context = dispatcherProvider.dispatcher) {
            themeModeRepository.emitThemeMode(themeMode = ThemeMode.Light)

            val actualThemeMode =
                settingsViewModel.settingsUiState
                    .map { uiState: SettingsUiState -> uiState.selectedThemeMode }
                    .first { themeMode: ThemeMode -> themeMode == ThemeMode.Light }

            assertEquals(ThemeMode.Light, actualThemeMode)
        }

    @Test
    fun `onThemeModeSelected calls repository setThemeMode`() =
        runTest(context = dispatcherProvider.dispatcher) {
            settingsViewModel.onThemeModeSelected(themeMode = ThemeMode.Dark)

            val actualLastSetThemeMode = themeModeRepository.lastSetThemeMode
            assertEquals(ThemeMode.Dark, actualLastSetThemeMode)
        }
}