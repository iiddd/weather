package com.iiddd.weather.favorites.presentation.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.theme.WeatherTheme
import com.iiddd.weather.core.theme.WeatherThemeTokens
import com.iiddd.weather.favorites.R

@Composable
fun FavoritesEmptyScreenContent(
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens

    Surface(
        modifier = modifier.fillMaxSize(),
        color = WeatherThemeTokens.colors.background,
        contentColor = WeatherThemeTokens.colors.onBackground,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = dimens.spacingExtraLarge),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.height(height = dimens.iconSizeExtraLarge),
                tint = WeatherThemeTokens.colors.onSurfaceVariant.copy(alpha = 0.6f),
            )

            Spacer(modifier = Modifier.height(height = dimens.spacingMedium))

            Text(
                text = stringResource(id = R.string.favorites_empty_title),
                style = WeatherThemeTokens.typography.headlineSmall,
                color = WeatherThemeTokens.colors.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(height = dimens.spacingSmall))

            Text(
                text = stringResource(id = R.string.favorites_empty_description),
                style = WeatherThemeTokens.typography.bodyMedium,
                color = WeatherThemeTokens.colors.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@WeatherPreview
@Composable
private fun FavoritesEmptyScreenContentPreview() {
    WeatherTheme {
        FavoritesEmptyScreenContent()
    }
}