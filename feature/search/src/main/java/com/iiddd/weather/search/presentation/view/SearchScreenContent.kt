package com.iiddd.weather.search.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.search.presentation.view.component.MarkerInfoCard
import com.iiddd.weather.search.presentation.view.component.SearchBar
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState
import kotlin.math.roundToInt

private val MARKER_VERTICAL_OFFSET_DP = 16.dp
private val MAP_PREVIEW_BACKGROUND_COLOR = Color(0xFFEDEDED)

@Composable
fun SearchScreenContent(
    searchUiState: SearchUiState,
    cameraPositionState: CameraPositionState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearMarker: () -> Unit,
    onOpenDetails: (latitude: Double, longitude: Double) -> Unit,
) {
    val isPreview = LocalInspectionMode.current
    val density = LocalDensity.current

    var mapSize by remember { mutableStateOf(Size(0f, 0f)) }
    var cardSize by remember { mutableStateOf(Size(0f, 0f)) }

    val markerOffsetPx = with(density) { MARKER_VERTICAL_OFFSET_DP.toPx() }
    val tailHeightPx = with(density) { MARKER_VERTICAL_OFFSET_DP.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { layoutCoordinates ->
                mapSize = Size(
                    layoutCoordinates.size.width.toFloat(),
                    layoutCoordinates.size.height.toFloat(),
                )
            },
    ) {
        // TODO: Unite into a single composable when GoogleMap supports preview mode
        if (isPreview) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MAP_PREVIEW_BACKGROUND_COLOR),
            )
        } else {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
            )
        }

        SearchBar(
            searchUiState = searchUiState,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(all = 8.dp),
        )

        searchUiState.marker?.let { markerLatLng: LatLng ->
            val screenPosition = remember(
                mapSize,
                cameraPositionState.position,
                markerLatLng,
            ) {
                computeScreenPosition(
                    marker = markerLatLng,
                    cameraPosition = cameraPositionState.position,
                    mapWidth = mapSize.width,
                    mapHeight = mapSize.height,
                )
            }

            MarkerInfoCard(
                title = searchUiState.markerTitle ?: searchUiState.query.ifBlank { "Result" },
                markerLatLng = markerLatLng,
                onOpenDetails = { latitude: Double?, longitude: Double? ->
                    onOpenDetails(
                        latitude ?: markerLatLng.latitude,
                        longitude ?: markerLatLng.longitude,
                    )
                },
                onClearMarker = onClearMarker,
                tailHeight = MARKER_VERTICAL_OFFSET_DP,
                modifier = Modifier
                    .onGloballyPositioned { layoutCoordinates ->
                        cardSize = Size(
                            layoutCoordinates.size.width.toFloat(),
                            layoutCoordinates.size.height.toFloat(),
                        )
                    }
                    .let { baseModifier ->
                        if (mapSize.width > 0f && mapSize.height > 0f && cardSize.width > 0f) {
                            val xOffset = (screenPosition.x - cardSize.width / 2.0).roundToInt()
                            val yOffset =
                                (screenPosition.y - cardSize.height - tailHeightPx - markerOffsetPx).roundToInt()
                            baseModifier.offset { IntOffset(x = xOffset, y = yOffset) }
                        } else {
                            baseModifier
                        }
                    },
            )
        }
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
            onQueryChange = {},
            onSearch = {},
            onClearMarker = {},
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
            onQueryChange = {},
            onSearch = {},
            onClearMarker = {},
            onOpenDetails = { _, _ -> },
        )
    }
}
