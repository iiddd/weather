package com.iiddd.weather.settings.presentation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.theme.ThemeMode
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.settings.presentation.view.component.ThemeSelectionRow

@Composable
fun SettingsScreenContent(
    title: String,
    selectedThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val typography = WeatherThemeTokens.typography

    Column(
        modifier = modifier.padding(all = 16.dp),
    ) {
        Text(
            text = title,
            style = typography.titleLarge,
        )

        Spacer(modifier = Modifier.height(height = 16.dp))

        Text(
            text = "Theme",
            style = typography.titleMedium,
        )

        Spacer(modifier = Modifier.height(height = 12.dp))

        ThemeSelectionRow(
            selectedThemeMode = selectedThemeMode,
            onThemeModeSelected = onThemeModeSelected,
        )
    }
}