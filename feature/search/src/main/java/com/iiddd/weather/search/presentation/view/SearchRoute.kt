package com.iiddd.weather.search.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

private const val DEFAULT_LATITUDE = 52.35
private const val DEFAULT_LONGITUDE = 4.91
private const val DEFAULT_ZOOM = 6f
private const val MARKER_ZOOM = 12f

@Composable
fun SearchRoute(
    searchViewModel: SearchViewModel = koinViewModel(),
    onOpenDetails: (latitude: Double, longitude: Double) -> Unit,
) {
    val searchUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle()

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE),
            DEFAULT_ZOOM,
        )
    }

    LaunchedEffect(key1 = searchUiState.marker) {
        searchUiState.marker?.let { markerLatLng: LatLng ->
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(markerLatLng, MARKER_ZOOM)
            )
        }
    }

    SearchScreen(
        searchUiState = searchUiState,
        cameraPositionState = cameraPositionState,
        onQueryChange = searchViewModel::onQueryChange,
        onSearch = searchViewModel::onSearch,
        onClearMarker = searchViewModel::onClearMarker,
        onRetrySearch = searchViewModel::onRetrySearch,
        onOpenDetails = onOpenDetails,
    )
}
