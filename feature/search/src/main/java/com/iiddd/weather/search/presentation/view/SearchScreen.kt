package com.iiddd.weather.search.presentation.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel(),
    onOpenDetails: (name: String?, lat: Double, lon: Double) -> Unit = { _, _, _ -> }
) {
    val defaultLat = 52.35
    val defaultLon = 4.91

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(defaultLat, defaultLon), 6f)
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.marker) {
        uiState.marker?.let { latLng ->
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        }
    }

    SearchContent(
        uiState = uiState,
        cameraPositionState = cameraPositionState,
        onQueryChange = viewModel::onQueryChange,
        onSearch = viewModel::search,
        onClearMarker = viewModel::clearMarker,
        onOpenDetails = { name, lat, lon ->
            onOpenDetails(name, lat, lon)
        }
    )
}