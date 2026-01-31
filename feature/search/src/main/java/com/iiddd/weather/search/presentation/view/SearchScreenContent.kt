package com.iiddd.weather.search.presentation.view

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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.search.R as SearchR
import com.iiddd.weather.search.presentation.view.component.Map
import com.iiddd.weather.search.presentation.view.component.MarkerInfoCard
import com.iiddd.weather.search.presentation.view.component.SearchBar
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState
import kotlin.math.roundToInt

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
    val dimens = WeatherThemeTokens.dimens

    var mapSize by remember { mutableStateOf(Size(0f, 0f)) }
    var cardSize by remember { mutableStateOf(Size(0f, 0f)) }

    val markerVerticalOffset = dimens.spacingLarge
    val markerOffsetPx = with(density) { markerVerticalOffset.toPx() }
    val tailHeightPx = with(density) { markerVerticalOffset.toPx() }

    val defaultMarkerTitle = stringResource(id = SearchR.string.marker_default_title)

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
        Map(
            isPreview = isPreview,
            cameraPositionState = cameraPositionState,
        )

        SearchBar(
            searchUiState = searchUiState,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(all = dimens.spacingSmall),
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
                title = searchUiState.markerTitle ?: searchUiState.query.ifBlank { defaultMarkerTitle },
                markerLatLng = markerLatLng,
                onOpenDetails = { latitude: Double?, longitude: Double? ->
                    onOpenDetails(
                        latitude ?: markerLatLng.latitude,
                        longitude ?: markerLatLng.longitude,
                    )
                },
                onClearMarker = onClearMarker,
                tailHeight = markerVerticalOffset,
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
