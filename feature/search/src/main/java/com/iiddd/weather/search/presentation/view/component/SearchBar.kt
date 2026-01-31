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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.search.R as SearchR
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState

@Composable
fun SearchBar(
    searchUiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val dimens = WeatherThemeTokens.dimens

    Column(modifier = modifier) {
        Card(elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationLarge)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = dimens.spacingSmall),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    value = searchUiState.query,
                    onValueChange = { query: String -> onQueryChange(query) },
                    placeholder = {
                        Text(
                            text = stringResource(id = SearchR.string.search_city_placeholder),
                            style = WeatherThemeTokens.typography.bodyLarge,
                        )
                    },
                    modifier = Modifier.weight(weight = 1f),
                    singleLine = true,
                    textStyle = WeatherThemeTokens.typography.bodyLarge,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchUiState.query.isBlank() || searchUiState.isLoading) return@KeyboardActions
                            softwareKeyboardController?.hide()
                            onSearch()
                        },
                    ),
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                if (searchUiState.query.isBlank() || searchUiState.isLoading) return@IconButton
                                softwareKeyboardController?.hide()
                                onSearch()
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = stringResource(id = SearchR.string.search_icon_content_description),
                            )
                        }
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(height = dimens.spacingSmall))

        when {
            searchUiState.isLoading -> {
                Card(elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationMedium)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = dimens.spacingSmall),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(all = dimens.spacingExtraSmall),
                            color = WeatherThemeTokens.colors.primary,
                        )
                        Spacer(modifier = Modifier.width(width = dimens.spacingSmall))
                        Text(
                            text = stringResource(id = SearchR.string.search_loading_text),
                            style = WeatherThemeTokens.typography.bodyMedium,
                        )
                    }
                }
            }

            searchUiState.errorMessage != null -> {
                Card(elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationMedium)) {
                    Text(
                        modifier = Modifier.padding(all = dimens.spacingSmall),
                        text = searchUiState.errorMessage,
                        style = WeatherThemeTokens.typography.bodyMedium,
                        color = WeatherThemeTokens.colors.error,
                    )
                }
            }
        }
    }
}

@WeatherPreview
@Composable
private fun SearchBarPreview() {
    val searchUiState = SearchUiState(
        query = "Amsterdam",
        marker = null,
        markerTitle = null,
        isLoading = false,
        errorMessage = null,
    )

    WeatherTheme {
        SearchBar(
            searchUiState = searchUiState,
            onQueryChange = {},
            onSearch = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = WeatherThemeTokens.dimens.spacingSmall),
        )
    }
}

@WeatherPreview
@Composable
private fun SearchBarPreviewLoading() {
    val searchUiState = SearchUiState(
        query = "Amsterdam",
        marker = null,
        markerTitle = null,
        isLoading = true,
        errorMessage = null,
    )

    WeatherTheme {
        SearchBar(
            searchUiState = searchUiState,
            onQueryChange = {},
            onSearch = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = WeatherThemeTokens.dimens.spacingSmall),
        )
    }
}