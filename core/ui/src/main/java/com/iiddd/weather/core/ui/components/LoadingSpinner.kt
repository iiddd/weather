package com.iiddd.weather.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun LoadingSpinner(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    indicatorScale: Float = 2f,
    strokeWidth: Dp = 2.dp
) {
    if (!isLoading) return

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

@Preview
@Composable
private fun LoadingSpinnerPreview() {
    LoadingSpinner(isLoading = true)
}