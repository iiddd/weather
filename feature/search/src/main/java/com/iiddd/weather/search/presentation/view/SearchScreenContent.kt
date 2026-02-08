package com.iiddd.weather.search.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.search.R as SearchR
import com.iiddd.weather.search.presentation.view.component.LocationBottomPanel
import com.iiddd.weather.search.presentation.view.component.Map
import com.iiddd.weather.search.presentation.view.component.MyLocationButton
import com.iiddd.weather.search.presentation.view.component.SearchBar
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState

@Composable
fun SearchScreenContent(
    searchUiState: SearchUiState,
    cameraPositionState: CameraPositionState,
    isMyLocationEnabled: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearMarker: () -> Unit,
    onMyLocationButtonClick: () -> Unit,
    onOpenDetails: (latitude: Double, longitude: Double) -> Unit,
) {
    val isPreview = LocalInspectionMode.current
    val dimens = WeatherThemeTokens.dimens
    val defaultMarkerTitle = stringResource(id = SearchR.string.marker_default_title)

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Map(
            isPreview = isPreview,
            cameraPositionState = cameraPositionState,
            markerPosition = searchUiState.marker,
        )

        SearchBar(
            searchUiState = searchUiState,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(all = dimens.spacingExtraLarge),
        )

        if (isMyLocationEnabled) {
            MyLocationButton(
                onClick = onMyLocationButtonClick,
                modifier = Modifier
                    .align(alignment = Alignment.BottomEnd)
                    .navigationBarsPadding()
                    .padding(all = dimens.spacingExtraLarge),
            )
        }

        // Bottom panel for location details
        LocationBottomPanel(
            locationTitle = searchUiState.markerTitle ?: searchUiState.query.ifBlank { defaultMarkerTitle },
            isVisible = searchUiState.marker != null,
            onViewDetails = {
                searchUiState.marker?.let { markerLatLng ->
                    onOpenDetails(markerLatLng.latitude, markerLatLng.longitude)
                }
            },
            onDismiss = onClearMarker,
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
        )
    }
}

@WeatherPreview
@Composable
private fun SearchScreenContentPreview() {
    val searchUiState = SearchUiState(
        query = "Amsterdam",
        marker = LatLng(52.35, 4.91),
        markerTitle = "Amsterdam, Netherlands",
        isLoading = false,
        errorMessage = null,
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(52.35, 4.91), 6f)
    }

    WeatherTheme {
        SearchScreenContent(
            searchUiState = searchUiState,
            cameraPositionState = cameraPositionState,
            isMyLocationEnabled = false,
            onQueryChange = {},
            onSearch = {},
            onClearMarker = {},
            onMyLocationButtonClick = {},
            onOpenDetails = { _, _ -> },
        )
    }
}

@WeatherPreview
@Composable
private fun SearchScreenContentPreviewLoading() {
    val searchUiState = SearchUiState(
        query = "Amsterdam",
        marker = null,
        markerTitle = null,
        isLoading = true,
        errorMessage = null,
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(52.35, 4.91), 6f)
    }

    WeatherTheme {
        SearchScreenContent(
            searchUiState = searchUiState,
            cameraPositionState = cameraPositionState,
            isMyLocationEnabled = false,
            onQueryChange = {},
            onSearch = {},
            onClearMarker = {},
            onMyLocationButtonClick = {},
            onOpenDetails = { _, _ -> },
        )
    }
}
