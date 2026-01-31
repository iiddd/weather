package com.iiddd.weather.settings.presentation.view.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
    val dimens = WeatherThemeTokens.dimens

    val borderStroke: BorderStroke =
        if (isSelected) {
            BorderStroke(
                width = dimens.borderWidthMedium,
                color = colors.primary,
            )
        } else {
            BorderStroke(
                width = dimens.borderWidthThin,
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
            defaultElevation = dimens.elevationSmall,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = dimens.spacingMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = themeDrawableRes),
                contentDescription = stringResource(id = contentDescriptionRes),
                modifier = Modifier
                    .height(height = dimens.imageHeightMedium)
                    .align(alignment = Alignment.CenterHorizontally),
            )

            Text(
                text = stringResource(id = titleRes),
                style = typography.labelMedium,
                modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
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
            onClick = {},
        )
    }
}