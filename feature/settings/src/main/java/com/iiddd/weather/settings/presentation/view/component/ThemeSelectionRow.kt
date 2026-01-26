package com.iiddd.weather.settings.presentation.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.theme.ThemeMode
import com.iiddd.weather.settings.presentation.view.model.ThemeOptionUiModel

@Composable
fun ThemeSelectionRow(
    selectedThemeMode: ThemeMode,
    themeOptionUiModels: List<ThemeOptionUiModel>,
    onThemeModeSelected: (ThemeMode) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
    ) {
        themeOptionUiModels.forEach { themeOptionUiModel: ThemeOptionUiModel ->
            ThemeOptionCard(
                themeOptionUiModel = themeOptionUiModel,
                isSelected = themeOptionUiModel.themeMode == selectedThemeMode,
                onClick = {
                    onThemeModeSelected(themeOptionUiModel.themeMode)
                },
                modifier = Modifier.weight(weight = 1f),
            )
        }
    }
}