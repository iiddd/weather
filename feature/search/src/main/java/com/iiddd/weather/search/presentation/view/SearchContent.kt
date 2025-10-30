package com.iiddd.weather.search.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.search.presentation.view.component.MarkerInfoCard
import com.iiddd.weather.search.presentation.view.component.SearchBar
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState
import kotlin.math.roundToInt

@Composable
fun SearchContent(
    uiState: SearchUiState,
    cameraPositionState: CameraPositionState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearMarker: () -> Unit,
    onAddFavorite: (name: String?, lat: Double?, lon: Double?) -> Unit
) {
    val isPreview = LocalInspectionMode.current
    val density = LocalDensity.current

    var mapSize by remember { mutableStateOf(Size(0f, 0f)) }
    var cardSize by remember { mutableStateOf(Size(0f, 0f)) }

    val markerVerticalOffsetDp = 16.dp
    val markerOffsetPx = with(density) { markerVerticalOffsetDp.toPx() }
    val tailHeightPx = with(density) { markerVerticalOffsetDp.toPx() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                mapSize = Size(coords.size.width.toFloat(), coords.size.height.toFloat())
            }
    ) {
        if (isPreview) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = androidx.compose.ui.graphics.Color(0xFFEDEDED))
            )
        } else {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            )
        }

        SearchBar(
            uiState = uiState,
            onQueryChange = onQueryChange,
            onSearch = onSearch,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        uiState.marker?.let { markerLatLng ->
            val screenPos = remember(mapSize, cameraPositionState.position, markerLatLng) {
                computeScreenPosition(
                    marker = markerLatLng,
                    cameraPosition = cameraPositionState.position,
                    mapWidth = mapSize.width,
                    mapHeight = mapSize.height
                )
            }

            MarkerInfoCard(
                title = uiState.markerTitle ?: uiState.query.ifBlank { "Result" },
                markerLatLng = markerLatLng,
                onAddFavorite = onAddFavorite,
                onClearMarker = onClearMarker,
                tailHeight = markerVerticalOffsetDp,
                modifier = Modifier
                    .onGloballyPositioned { coords ->
                        cardSize = Size(coords.size.width.toFloat(), coords.size.height.toFloat())
                    }
                    .let { base ->
                        if (mapSize.width > 0f && mapSize.height > 0f && cardSize.width > 0f) {
                            val x = (screenPos.x - cardSize.width / 2.0).roundToInt()
                            val y =
                                (screenPos.y - cardSize.height - tailHeightPx - markerOffsetPx).roundToInt()
                            base.offset { IntOffset(x, y) }
                        } else base
                    }
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchContentPreview() {
    val fakeState = SearchUiState(
        query = "Amsterdam",
        marker = LatLng(52.35, 4.91),
        markerTitle = "Amsterdam, Netherlands",
        loading = false,
        error = null
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(52.35, 4.91), 6f)
    }

    WeatherTheme {
        SearchContent(
            uiState = fakeState,
            cameraPositionState = cameraPositionState,
            onQueryChange = {},
            onSearch = {},
            onClearMarker = {},
            onAddFavorite = { _, _, _ -> }
        )
    }
}