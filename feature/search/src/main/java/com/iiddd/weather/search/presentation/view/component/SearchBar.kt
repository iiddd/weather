package com.iiddd.weather.search.presentation.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState

@Composable
fun SearchBar(
    uiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = uiState.query,
                    onValueChange = { query: String -> onQueryChange(query) },
                    placeholder = { Text(text = "Search city") },
                    modifier = Modifier.weight(weight = 1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (uiState.query.isBlank() || uiState.loading) return@KeyboardActions
                            softwareKeyboardController?.hide()
                            onSearch()
                        }
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (uiState.query.isBlank() || uiState.loading) return@IconButton
                                softwareKeyboardController?.hide()
                                onSearch()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 8.dp))

        when {
            uiState.loading -> {
                Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.padding(all = 4.dp))
                        Spacer(modifier = Modifier.width(width = 8.dp))
                        Text(text = "Searching...")
                    }
                }
            }

            uiState.error != null -> {
                Card(elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                    Text(
                        modifier = Modifier.padding(all = 8.dp),
                        text = uiState.error
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun SearchBarPreview() {
    val fakeState = SearchUiState(
        query = "Amsterdam",
        marker = null,
        markerTitle = null,
        loading = false,
        error = null
    )

    WeatherTheme {
        SearchBar(
            uiState = fakeState,
            onQueryChange = {},
            onSearch = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
        )
    }
}