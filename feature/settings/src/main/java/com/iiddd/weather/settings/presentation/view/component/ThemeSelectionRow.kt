package com.iiddd.weather.settings.presentation.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.ThemeMode
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.settings.R as SettingsR

@Composable
fun ThemeSelectionRow(
    selectedThemeMode: ThemeMode,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens

    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(
            space = dimens.spacingMedium,
            alignment = Alignment.CenterHorizontally,
        ),
    ) {
        ThemeCard(
            isSelected = selectedThemeMode == ThemeMode.Light,
            themeDrawableRes = SettingsR.drawable.theme_light,
            titleRes = SettingsR.string.theme_light_label,
            contentDescriptionRes = SettingsR.string.theme_light_content_description,
            onClick = { onThemeModeSelected(ThemeMode.Light) },
            modifier = Modifier.weight(weight = 1f),
        )

        ThemeCard(
            isSelected = selectedThemeMode == ThemeMode.Dark,
            themeDrawableRes = SettingsR.drawable.theme_dark,
            titleRes = SettingsR.string.theme_dark_label,
            contentDescriptionRes = SettingsR.string.theme_dark_content_description,
            onClick = { onThemeModeSelected(ThemeMode.Dark) },
            modifier = Modifier.weight(weight = 1f),
        )

        ThemeCard(
            isSelected = selectedThemeMode == ThemeMode.System,
            themeDrawableRes = SettingsR.drawable.theme_system,
            titleRes = SettingsR.string.theme_system_label,
            contentDescriptionRes = SettingsR.string.theme_system_content_description,
            onClick = { onThemeModeSelected(ThemeMode.System) },
            modifier = Modifier.weight(weight = 1f),
        )
    }
}

@WeatherPreview
@Composable
private fun ThemeSelectionRowPreview() {
    WeatherTheme {
        ThemeSelectionRow(
            selectedThemeMode = ThemeMode.System,
            onThemeModeSelected = {},
        )
    }
}