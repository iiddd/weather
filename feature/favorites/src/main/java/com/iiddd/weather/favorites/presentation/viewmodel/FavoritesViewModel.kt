package com.iiddd.weather.favorites.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.preferences.favorites.FavoriteLocation
import com.iiddd.weather.core.preferences.favorites.FavoritesRepository
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.favorites.presentation.model.FavoriteLocationWithWeather
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository,
    private val weatherRepository: WeatherRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : ViewModel() {

    private val mutableFavoritesUiState: MutableStateFlow<FavoritesUiState> =
        MutableStateFlow(value = FavoritesUiState.Loading)

    val favoritesUiState: StateFlow<FavoritesUiState> =
        mutableFavoritesUiState.asStateFlow()

    fun onEvent(favoritesUiEvent: FavoritesUiEvent) {
        when (favoritesUiEvent) {
            FavoritesUiEvent.LoadFavoritesRequested -> {
                loadFavorites(forceRefresh = false)
            }

            FavoritesUiEvent.RefreshRequested -> {
                loadFavorites(forceRefresh = true)
            }

            is FavoritesUiEvent.DeleteFavoriteRequested -> {
                deleteFavorite(favoriteLocationWithWeather = favoritesUiEvent.favoriteLocationWithWeather)
            }
        }
    }

    private fun deleteFavorite(favoriteLocationWithWeather: FavoriteLocationWithWeather) {
        viewModelScope.launch(context = dispatcherProvider.io) {
            favoritesRepository.removeFavorite(
                latitude = favoriteLocationWithWeather.favoriteLocation.latitude,
                longitude = favoriteLocationWithWeather.favoriteLocation.longitude,
            )

            mutableFavoritesUiState.update { currentState ->
                when (currentState) {
                    is FavoritesUiState.Content -> {
                        val updatedFavorites = currentState.favorites.filterNot { favorite ->
                            favorite.favoriteLocation.id == favoriteLocationWithWeather.favoriteLocation.id
                        }
                        if (updatedFavorites.isEmpty()) {
                            FavoritesUiState.Empty
                        } else {
                            currentState.copy(favorites = updatedFavorites)
                        }
                    }
                    else -> currentState
                }
            }
        }
    }

    private fun loadFavorites(forceRefresh: Boolean) {
        viewModelScope.launch(context = dispatcherProvider.main) {
            val currentState = mutableFavoritesUiState.value

            if (forceRefresh && currentState is FavoritesUiState.Content) {
                mutableFavoritesUiState.update { state ->
                    (state as? FavoritesUiState.Content)?.copy(isRefreshing = true) ?: state
                }
            } else if (!forceRefresh) {
                mutableFavoritesUiState.value = FavoritesUiState.Loading
            }

            val favorites: List<FavoriteLocation> = withContext(context = dispatcherProvider.io) {
                favoritesRepository.favoritesFlow.first()
            }

            if (favorites.isEmpty()) {
                mutableFavoritesUiState.value = FavoritesUiState.Empty
                return@launch
            }

            val weatherResults: List<Pair<FavoriteLocation, ApiResult<Weather>>> =
                withContext(context = dispatcherProvider.io) {
                    favorites.map { favoriteLocation ->
                        async {
                            val weatherResult = weatherRepository.getWeather(
                                latitude = favoriteLocation.latitude,
                                longitude = favoriteLocation.longitude,
                            )
                            favoriteLocation to weatherResult
                        }
                    }.awaitAll()
                }

            val hasAnyError = weatherResults.any { (_, result) -> result is ApiResult.Failure }

            if (hasAnyError) {
                val firstError = weatherResults
                    .firstNotNullOfOrNull { (_, result) ->
                        (result as? ApiResult.Failure)?.error
                    } ?: ApiError.Unknown(message = "Failed to load weather for favorites")

                mutableFavoritesUiState.value = FavoritesUiState.Error(apiError = firstError)
                return@launch
            }

            val favoritesWithWeather = weatherResults.map { (favoriteLocation, weatherResult) ->
                val weather = (weatherResult as ApiResult.Success).value
                FavoriteLocationWithWeather(
                    favoriteLocation = favoriteLocation,
                    currentTemperature = weather.currentTemp,
                    weatherDescription = weather.description,
                    weatherIcon = weather.hourly.firstOrNull()?.icon,
                )
            }

            mutableFavoritesUiState.value = FavoritesUiState.Content(
                favorites = favoritesWithWeather,
                isRefreshing = false,
            )
        }
    }
}
