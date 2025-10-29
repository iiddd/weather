package com.iiddd.weather.search.presentation.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState

@Composable
fun SearchContent(
    uiState: SearchUiState,
    cameraPositionState: CameraPositionState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClearMarker: () -> Unit
) {
    val isPreview = LocalInspectionMode.current

    Box(modifier = Modifier.fillMaxSize()) {
        if (isPreview) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Map preview", modifier = Modifier.padding(16.dp))
            }
        } else {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState
            ) {
                uiState.marker?.let { pos ->
                    Marker(
                        state = MarkerState(position = pos),
                        title = uiState.query.ifBlank { "Result" }
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = uiState.query,
                        onValueChange = { onQueryChange(it) },
                        placeholder = { Text(text = "Search city") },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (uiState.query.isBlank() || uiState.loading) return@IconButton
                                    onSearch()
                                }
                            ) {
                                Icon(Icons.Filled.Search, contentDescription = "Search")
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when {
                uiState.loading -> {
                    Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "Searching...")
                        }
                    }
                }

                uiState.error != null -> {
                    Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = uiState.error
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchContentPreview() {
    val fakeState = SearchUiState(
        query = "Amsterdam",
        marker = LatLng(52.35, 4.91),
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
            onClearMarker = {}
        )
    }
}