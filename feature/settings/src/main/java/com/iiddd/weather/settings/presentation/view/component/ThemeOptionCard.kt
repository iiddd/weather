package com.iiddd.weather.settings.presentation.view.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.ThemeMode
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.settings.R
import com.iiddd.weather.settings.presentation.view.model.ThemeOptionUiModel

@Composable
fun ThemeOptionCard(
    themeOptionUiModel: ThemeOptionUiModel,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = WeatherThemeTokens.shapes.large,
) {
    val colors = WeatherThemeTokens.colors
    val typography = WeatherThemeTokens.typography

    val borderStroke: BorderStroke? =
        if (isSelected) {
            BorderStroke(
                width = 2.dp,
                color = colors.primary,
            )
        } else {
            null
        }

    val containerColor =
        if (isSelected) {
            colors.primaryContainer
        } else {
            colors.surface
        }

    val contentColor =
        if (isSelected) {
            colors.onPrimaryContainer
        } else {
            colors.onSurface
        }

    Card(
        modifier = modifier.clickable(
            role = Role.RadioButton,
            onClick = onClick,
        ),
        shape = shape,
        border = borderStroke,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = themeOptionUiModel.previewDrawableResourceId),
                contentDescription = themeOptionUiModel.title,
                modifier = Modifier.height(height = 120.dp),
                tint = contentColor,
            )

            Spacer(modifier = Modifier.height(height = 10.dp))

            Text(
                text = themeOptionUiModel.title,
                style = typography.labelLarge,
                color = contentColor,
            )
        }
    }
}

@WeatherPreview
@Composable
private fun ThemeOptionCardPreview() {
    ThemeOptionCard(
        themeOptionUiModel = ThemeOptionUiModel(
            themeMode = ThemeMode.System,
            title = "Light",
            previewDrawableResourceId = R.drawable.theme_light,
        ),
        isSelected = true,
        onClick = {},
    )
}