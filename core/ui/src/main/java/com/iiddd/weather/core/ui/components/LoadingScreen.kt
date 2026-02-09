package com.iiddd.weather.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import com.iiddd.weather.core.theme.WeatherTheme
import com.iiddd.weather.core.theme.WeatherThemeTokens

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
) {
    val colors = WeatherThemeTokens.colors
    val progressIndicatorScale = 2f

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colors.background,
        contentColor = colors.onBackground,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                color = colors.primary,
                strokeCap = StrokeCap.Round,
                strokeWidth = WeatherThemeTokens.dimens.borderWidthMedium,
                modifier = Modifier.scale(progressIndicatorScale)
            )
        }
    }
}

@WeatherPreview
@Composable
private fun LoadingScreenPreview() {
    WeatherTheme {
        LoadingScreen()
    }
}