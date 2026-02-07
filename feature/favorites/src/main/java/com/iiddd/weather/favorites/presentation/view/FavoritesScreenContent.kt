package com.iiddd.weather.favorites.presentation.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.iiddd.weather.core.preferences.favorites.FavoriteLocation
import com.iiddd.weather.core.ui.components.WeatherPreview
import com.iiddd.weather.core.ui.theme.WeatherTheme
import com.iiddd.weather.core.ui.theme.WeatherThemeTokens
import com.iiddd.weather.favorites.presentation.model.FavoriteLocationWithWeather
import com.iiddd.weather.favorites.presentation.view.component.FavoriteLocationCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreenContent(
    favorites: List<FavoriteLocationWithWeather>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onFavoriteClicked: (FavoriteLocationWithWeather) -> Unit,
    onDeleteFavorite: (FavoriteLocationWithWeather) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens

    Surface(
        modifier = modifier.fillMaxSize(),
        color = WeatherThemeTokens.colors.background,
        contentColor = WeatherThemeTokens.colors.onBackground,
    ) {
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = rememberScrollState())
                    .padding(all = dimens.spacingExtraLarge),
                verticalArrangement = Arrangement.spacedBy(space = dimens.spacingSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                favorites.forEach { favoriteLocationWithWeather ->
                    SwipeToDeleteFavoriteItem(
                        favoriteLocationWithWeather = favoriteLocationWithWeather,
                        onFavoriteClicked = onFavoriteClicked,
                        onDeleteFavorite = onDeleteFavorite,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeToDeleteFavoriteItem(
    favoriteLocationWithWeather: FavoriteLocationWithWeather,
    onFavoriteClicked: (FavoriteLocationWithWeather) -> Unit,
    onDeleteFavorite: (FavoriteLocationWithWeather) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dimens = WeatherThemeTokens.dimens
    val dismissState = rememberSwipeToDismissBoxState()

    LaunchedEffect(key1 = dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            onDeleteFavorite(favoriteLocationWithWeather)
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val backgroundColor by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    SwipeToDismissBoxValue.EndToStart -> WeatherThemeTokens.colors.error
                    else -> Color.Transparent
                },
                label = "SwipeBackgroundColor",
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(size = dimens.cornerRadiusMedium))
                    .background(color = backgroundColor)
                    .padding(horizontal = dimens.spacingExtraLarge),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = WeatherThemeTokens.colors.onError,
                )
            }
        },
        content = {
            FavoriteLocationCard(
                favoriteLocationWithWeather = favoriteLocationWithWeather,
                onClick = { onFavoriteClicked(favoriteLocationWithWeather) },
            )
        },
    )
}

@WeatherPreview
@Composable
private fun FavoritesScreenContentPreview() {
    WeatherTheme {
        FavoritesScreenContent(
            favorites = listOf(
                FavoriteLocationWithWeather(
                    favoriteLocation = FavoriteLocation(
                        id = "1",
                        cityName = "Tokyo, Japan",
                        latitude = 35.6762,
                        longitude = 139.6503,
                    ),
                    currentTemperature = 22,
                    weatherDescription = "Clear",
                    weatherIcon = "01d",
                ),
                FavoriteLocationWithWeather(
                    favoriteLocation = FavoriteLocation(
                        id = "2",
                        cityName = "New York, USA",
                        latitude = 40.7128,
                        longitude = -74.0060,
                    ),
                    currentTemperature = 15,
                    weatherDescription = "Cloudy",
                    weatherIcon = "03d",
                ),
            ),
            isRefreshing = false,
            onRefresh = {},
            onFavoriteClicked = {},
            onDeleteFavorite = {},
        )
    }
}



