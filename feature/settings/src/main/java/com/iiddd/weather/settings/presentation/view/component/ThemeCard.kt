package com.iiddd.weather.settings.presentation.view.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.settings.R as SettingsR

@Composable
internal fun ThemeCard(
    isSelected: Boolean = false,
    @DrawableRes themeDrawableRes: Int,
    @StringRes textRes : Int,
    @StringRes contentDescriptionRes: Int
) {
    Card(
        modifier = Modifier
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) WeatherThemeTokens.colors.onPrimary else WeatherThemeTokens.colors.onSecondary,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = themeDrawableRes),
                contentDescription = stringResource(contentDescriptionRes),
                modifier = Modifier.height(120.dp)
            )

            Text(
                text = stringResource(textRes),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@WeatherPreview
@Composable
private fun ThemeCardSelectedPreview() {
    WeatherTheme {
        ThemeCard(
            isSelected = true,
            themeDrawableRes = SettingsR.drawable.theme_system,
            textRes = SettingsR.string.theme_system_label,
            contentDescriptionRes = SettingsR.string.theme_system_content_description
        )
    }
}

@WeatherPreview
@Composable
private fun ThemeCardUnselectedPreview() {
    WeatherTheme {
        ThemeCard(
            isSelected = false,
            themeDrawableRes = SettingsR.drawable.theme_system,
            textRes = SettingsR.string.theme_system_label,
            contentDescriptionRes = SettingsR.string.theme_system_content_description
        )
    }
}