package com.iiddd.weather.settings.presentation.view.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.settings.R as SettingsR

@Composable
internal fun ThemeCard(
    isSelected: Boolean,
    @DrawableRes themeDrawableRes: Int,
    @StringRes titleRes: Int,
    @StringRes contentDescriptionRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = WeatherThemeTokens.colors
    val typography = WeatherThemeTokens.typography
    val shapes = WeatherThemeTokens.shapes

    val borderStroke: BorderStroke? =
        if (isSelected) {
            BorderStroke(
                width = 2.dp,
                color = colors.primary,
            )
        } else {
            BorderStroke(
                width = 1.dp,
                color = colors.outlineVariant,
            )
        }

    val containerColor =
        if (isSelected) colors.primaryContainer else colors.surface

    val contentColor =
        if (isSelected) colors.onPrimaryContainer else colors.onSurface

    Card(
        modifier = modifier.selectable(
            selected = isSelected,
            role = Role.RadioButton,
            onClick = onClick,
        ),
        shape = shapes.large,
        border = borderStroke,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = themeDrawableRes),
                contentDescription = stringResource(id = contentDescriptionRes),
                modifier = Modifier
                    .height(120.dp)
                    .align(Alignment.CenterHorizontally),
            )

            Text(
                text = stringResource(id = titleRes),
                style = typography.labelMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

@WeatherPreview
@Composable
private fun ThemeCardPreview() {
    WeatherTheme {
        ThemeCard(
            isSelected = false,
            themeDrawableRes = SettingsR.drawable.theme_system,
            titleRes = SettingsR.string.theme_system_label,
            contentDescriptionRes = SettingsR.string.theme_system_content_description,
            onClick = {}
        )
    }
}