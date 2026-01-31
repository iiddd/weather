package com.iiddd.weather.search.presentation.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens

@Composable
internal fun Map(
    cameraPositionState: CameraPositionState,
    isPreview: Boolean = false,
    modifier: Modifier = Modifier,
) {
    if (isPreview) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = WeatherThemeTokens.colors.surfaceVariant),
        )
    } else {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        )
    }
}