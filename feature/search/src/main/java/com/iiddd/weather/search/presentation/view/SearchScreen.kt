package com.iiddd.weather.search.presentation.view

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.core.ui.components.ErrorScreen
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState

@Composable
fun SearchScreen(
    searchUiState: SearchUiState,
    cameraPositionState: CameraPositionState,
    isMyLocationEnabled: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearMarker: () -> Unit,
    onRetrySearch: () -> Unit,
    onMyLocationButtonClick: () -> Unit,
    onOpenDetails: (latitude: Double, longitude: Double) -> Unit,
) {
    if (searchUiState.errorMessage != null && searchUiState.marker == null) {
        ErrorScreen(
            errorMessage = searchUiState.errorMessage,
            onRetry = onRetrySearch,
        )
        return
    }

    SearchScreenContent(
        searchUiState = searchUiState,
        cameraPositionState = cameraPositionState,
        isMyLocationEnabled = isMyLocationEnabled,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        onClearMarker = onClearMarker,
        onMyLocationButtonClick = onMyLocationButtonClick,
        onOpenDetails = onOpenDetails,
    )
}

@WeatherPreview
@Composable
private fun SearchScreenPreviewWithMarker() {
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
        SearchScreen(
            searchUiState = searchUiState,
            cameraPositionState = cameraPositionState,
            isMyLocationEnabled = false,
            onQueryChange = {},
            onSearch = {},
            onClearMarker = {},
            onRetrySearch = {},
            onMyLocationButtonClick = {},
            onOpenDetails = { _, _ -> },
        )
    }
}

@WeatherPreview
@Composable
private fun SearchScreenPreviewError() {
    val searchUiState = SearchUiState(
        query = "InvalidCity",
        marker = null,
        markerTitle = null,
        isLoading = false,
        errorMessage = "Unable to find location. Please try again.",
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(52.35, 4.91), 6f)
    }

    WeatherTheme {
        SearchScreen(
            searchUiState = searchUiState,
            cameraPositionState = cameraPositionState,
            isMyLocationEnabled = false,
            onQueryChange = {},
            onSearch = {},
            onClearMarker = {},
            onRetrySearch = {},
            onMyLocationButtonClick = {},
            onOpenDetails = { _, _ -> },
        )
    }
}