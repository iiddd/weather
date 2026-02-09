package com.iiddd.weather.search.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.location.domain.GeocodingService
import com.iiddd.weather.location.domain.LocationTracker
import com.iiddd.weather.search.presentation.viewmodel.SearchViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

private const val DEFAULT_LATITUDE = 52.35
private const val DEFAULT_LONGITUDE = 4.91
private const val DEFAULT_ZOOM = 6f
private const val MARKER_ZOOM = 12f
private const val MY_LOCATION_FALLBACK_TITLE = "My Location"

@Composable
fun SearchRoute(
    searchViewModel: SearchViewModel = koinViewModel(),
    locationTracker: LocationTracker = koinInject(),
    geocodingService: GeocodingService = koinInject(),
    onOpenDetails: (latitude: Double, longitude: Double) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val searchUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle()

    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

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
        isMyLocationEnabled = hasLocationPermission,
        onQueryChange = searchViewModel::onQueryChange,
        onSearch = searchViewModel::onSearch,
        onClearMarker = searchViewModel::onClearMarker,
        onRetrySearch = searchViewModel::onRetrySearch,
        onMyLocationButtonClick = {
            coroutineScope.launch {
                val currentLocation = locationTracker.getLastKnownLocation()
                currentLocation?.let { coordinates ->
                    searchViewModel.onSetMarkerAtCurrentLocation(
                        latitude = coordinates.latitude,
                        longitude = coordinates.longitude,
                        locationTitle = MY_LOCATION_FALLBACK_TITLE,
                    )

                    val locationTitle = geocodingService.reverseGeocode(
                        latitude = coordinates.latitude,
                        longitude = coordinates.longitude,
                    )
                    if (locationTitle != null) {
                        searchViewModel.onUpdateMarkerTitle(locationTitle = locationTitle)
                    }
                }
            }
        },
        onOpenDetails = onOpenDetails,
    )
}
