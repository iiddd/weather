package com.iiddd.weather.search.presentation.view.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.search.presentation.viewmodel.SearchUiState
import com.iiddd.weather.search.R as SearchR

@Composable
fun SearchBar(
    searchUiState: SearchUiState,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    val dimens = WeatherThemeTokens.dimens
    val colors = WeatherThemeTokens.colors
    val typography = WeatherThemeTokens.typography
    val shapes = WeatherThemeTokens.shapes

    val isSearchEnabled: Boolean = searchUiState.query.isNotBlank() && !searchUiState.isLoading

    Column(modifier = modifier) {
        Card(
            shape = shapes.medium,
            colors = CardDefaults.cardColors(containerColor = colors.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevationLarge),
        ) {
            TextField(
                value = searchUiState.query,
                onValueChange = { query: String -> onQueryChange(query) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = typography.bodyLarge,
                placeholder = {
                    Text(
                        text = stringResource(id = SearchR.string.search_city_placeholder),
                        style = typography.bodyLarge,
                        color = colors.onSurfaceVariant,
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(id = SearchR.string.search_icon_content_description),
                        tint = colors.onSurfaceVariant,
                    )
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (searchUiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(size = dimens.iconSizeMedium),
                                strokeWidth = dimens.spacingExtraSmall,
                            )
                            Spacer(modifier = Modifier.width(width = dimens.spacingSmall))
                        } else if (searchUiState.query.isNotBlank()) {
                            IconButton(onClick = { onQueryChange("") }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = stringResource(id = SearchR.string.search_clear_content_description),
                                    tint = colors.onSurfaceVariant,
                                )
                            }
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        if (!isSearchEnabled) return@KeyboardActions
                        softwareKeyboardController?.hide()
                        onSearch()
                    },
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = colors.primary,
                    focusedTextColor = colors.onSurface,
                    unfocusedTextColor = colors.onSurface,
                ),
            )
        }

        if (searchUiState.errorMessage != null) {
            Spacer(modifier = Modifier.height(height = dimens.spacingMedium))

            Card(
                shape = shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = colors.errorContainer,
                    contentColor = colors.onErrorContainer,
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = dimens.elevationMedium,
                ),
            ) {
                Text(
                    modifier = Modifier.padding(all = dimens.spacingMedium),
                    text = searchUiState.errorMessage,
                    style = typography.bodyMedium,
                )
            }
        }
    }
}

@WeatherPreview
@Composable
private fun SearchBarPreviewContent() {
    val mockSearchUiState = SearchUiState(
        query = "New York",
        isLoading = false,
        errorMessage = null,
    )

    WeatherTheme {
        SearchBar(
            searchUiState = mockSearchUiState,
            onQueryChange = {},
            onSearch = {},
        )
    }
}