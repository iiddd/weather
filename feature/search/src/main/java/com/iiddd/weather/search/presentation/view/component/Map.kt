package com.iiddd.weather.search.presentation.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.maps.android.compose.GoogleMap

private val MAP_PREVIEW_BACKGROUND_COLOR = Color(0xFFEDEDED)

@Composable
internal fun Map(
    cameraPositionState: com.google.maps.android.compose.CameraPositionState,
    isPreview: Boolean = false,
    modifier: Modifier = Modifier
) {
    if (isPreview) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = MAP_PREVIEW_BACKGROUND_COLOR),
        )
    } else {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
        )
    }
}