package com.iiddd.weather.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.theme.WeatherTheme

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier,
    indicatorScale: Float = 2f,
    strokeWidth: Dp = 2.dp
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.scale(scale = indicatorScale),
            strokeWidth = strokeWidth,
            strokeCap = StrokeCap.Round
        )
    }
}

@WeatherPreview
@Composable
private fun LoadingScreenPreview() {
    WeatherTheme {
        LoadingScreen()
    }
}