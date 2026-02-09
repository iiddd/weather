package com.iiddd.weather.search.presentation.view.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.iiddd.weather.core.theme.WeatherThemeTokens

@Composable
internal fun Map(
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier,
    isPreview: Boolean = false,
    markerPosition: LatLng? = null,
) {
    if (isPreview) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(color = WeatherThemeTokens.colors.surfaceVariant),
        )
    } else {
        GoogleMap(
            modifier = modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(
                isMyLocationEnabled = false,
            ),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = false,
                zoomControlsEnabled = false,
            ),
        ) {
            markerPosition?.let { position ->
                val markerState = rememberUpdatedMarkerState(position = position)
                Marker(
                    state = markerState,
                )
            }
        }
    }
}