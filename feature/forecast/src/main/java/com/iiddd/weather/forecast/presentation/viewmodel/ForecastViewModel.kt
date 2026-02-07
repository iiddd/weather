package com.iiddd.weather.forecast.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.preferences.favorites.FavoriteLocation
import com.iiddd.weather.core.preferences.favorites.FavoritesRepository
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import com.iiddd.weather.location.domain.Coordinates
import com.iiddd.weather.location.domain.GeocodingService
import com.iiddd.weather.location.domain.LocationTracker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class ForecastViewModel(
    private val weatherRepository: WeatherRepository,
    private val geocodingService: GeocodingService,
    private val locationTracker: LocationTracker,
    private val favoritesRepository: FavoritesRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : ViewModel() {

    private val mutableForecastUiState: MutableStateFlow<ForecastUiState> =
        MutableStateFlow(value = ForecastUiState.Loading)

    val forecastUiState: StateFlow<ForecastUiState> =
        mutableForecastUiState.asStateFlow()

    private var lastRequestParameters: RequestParameters? = null

    fun onEvent(forecastUiEvent: ForecastUiEvent) {
        when (forecastUiEvent) {
            is ForecastUiEvent.LoadWeatherRequested -> {
                val newRequestParameters = RequestParameters(
                    latitude = forecastUiEvent.latitude,
                    longitude = forecastUiEvent.longitude,
                    useDeviceLocation = forecastUiEvent.useDeviceLocation,
                )

                val hasContent = mutableForecastUiState.value is ForecastUiState.Content
                if (hasContent && newRequestParameters == lastRequestParameters) {
                    return
                }

                lastRequestParameters = newRequestParameters

                loadWeather(
                    latitude = forecastUiEvent.latitude,
                    longitude = forecastUiEvent.longitude,
                    useDeviceLocation = forecastUiEvent.useDeviceLocation,
                    forceRefresh = false,
                )
            }

            ForecastUiEvent.RefreshRequested -> {
                val parameters = lastRequestParameters ?: return

                loadWeather(
                    latitude = parameters.latitude,
                    longitude = parameters.longitude,
                    useDeviceLocation = parameters.useDeviceLocation,
                    forceRefresh = true,
                )
            }

            ForecastUiEvent.ErrorDismissed -> {
                if (mutableForecastUiState.value is ForecastUiState.Error) {
                    mutableForecastUiState.value = ForecastUiState.Loading
                }
            }

            ForecastUiEvent.ToggleFavoriteRequested -> {
                toggleFavorite()
            }
        }
    }

    private fun toggleFavorite() {
        val currentState = mutableForecastUiState.value as? ForecastUiState.Content ?: return
        val weather = currentState.weather
        val parameters = lastRequestParameters ?: return

        val latitude = parameters.latitude ?: return
        val longitude = parameters.longitude ?: return

        viewModelScope.launch(context = dispatcherProvider.io) {
            val isFavorite = favoritesRepository.isFavoriteFlow(
                latitude = latitude,
                longitude = longitude,
            ).first()

            if (isFavorite) {
                favoritesRepository.removeFavorite(
                    latitude = latitude,
                    longitude = longitude,
                )
            } else {
                val cityName = weather.city ?: "Unknown Location"
                favoritesRepository.addFavorite(
                    favoriteLocation = FavoriteLocation(
                        id = UUID.randomUUID().toString(),
                        cityName = cityName,
                        latitude = latitude,
                        longitude = longitude,
                    )
                )
            }

            mutableForecastUiState.update { state ->
                (state as? ForecastUiState.Content)?.copy(isFavorite = !isFavorite) ?: state
            }
        }
    }

    private fun loadWeather(
        latitude: Double?,
        longitude: Double?,
        useDeviceLocation: Boolean,
        forceRefresh: Boolean,
    ) {
        viewModelScope.launch(context = dispatcherProvider.main) {
            val currentState = mutableForecastUiState.value

            if (forceRefresh && currentState is ForecastUiState.Content) {
                mutableForecastUiState.update { state ->
                    (state as? ForecastUiState.Content)?.copy(isRefreshing = true) ?: state
                }
            } else if (!forceRefresh) {
                mutableForecastUiState.value = ForecastUiState.Loading
            }

            val coordinatesResult: ApiResult<Coordinates> = resolveCoordinatesWithRetry(
                latitude = latitude,
                longitude = longitude,
                useDeviceLocation = useDeviceLocation,
            )

            when (coordinatesResult) {
                is ApiResult.Success -> {
                    loadWeatherAndCityNameAndEmitState(
                        latitude = coordinatesResult.value.latitude,
                        longitude = coordinatesResult.value.longitude,
                    )
                }

                is ApiResult.Failure -> {
                    mutableForecastUiState.value = ForecastUiState.Error(
                        apiError = coordinatesResult.error,
                    )
                }
            }
        }
    }

    private suspend fun resolveCoordinatesWithRetry(
        latitude: Double?,
        longitude: Double?,
        useDeviceLocation: Boolean,
        maxRetries: Int = 3,
        retryDelayMilliseconds: Long = 500L,
    ): ApiResult<Coordinates> {
        // Explicit coordinates don't need retry
        if (latitude != null && longitude != null) {
            return ApiResult.Success(
                value = Coordinates(
                    latitude = latitude,
                    longitude = longitude,
                )
            )
        }

        if (!useDeviceLocation) {
            return ApiResult.Failure(
                error = ApiError.Input(
                    message = "No coordinates provided and device location not requested.",
                )
            )
        }

        // Retry logic for device location
        repeat(times = maxRetries) { attempt ->
            val result = resolveDeviceLocation()
            if (result is ApiResult.Success) {
                return result
            }

            if (attempt < maxRetries - 1) {
                delay(timeMillis = retryDelayMilliseconds)
            }
        }

        return ApiResult.Failure(
            error = ApiError.Input(
                message = "Location is unavailable. Please enable location services and grant location permission.",
            )
        )
    }

    private suspend fun resolveDeviceLocation(): ApiResult<Coordinates> =
        withContext(context = dispatcherProvider.io) {
            val lastKnownCoordinate = locationTracker.getLastKnownLocation()
            if (lastKnownCoordinate != null) {
                return@withContext ApiResult.Success(value = lastKnownCoordinate)
            }

            val currentLocation = locationTracker.getCurrentLocationOrNull()
            if (currentLocation != null) {
                return@withContext ApiResult.Success(value = currentLocation)
            }

            ApiResult.Failure(
                error = ApiError.Input(
                    message = "Location is unavailable.",
                )
            )
        }

    private suspend fun loadWeatherAndCityNameAndEmitState(
        latitude: Double,
        longitude: Double,
    ) {
        val result: Triple<ApiResult<Weather>, String?, Boolean> =
            withContext(context = dispatcherProvider.io) {
                val weatherApiResult = weatherRepository.getWeather(
                    latitude = latitude,
                    longitude = longitude,
                )

                val resolvedCityNameResult = geocodingService.reverseGeocode(
                    latitude = latitude,
                    longitude = longitude,
                )

                val isFavoriteResult = favoritesRepository.isFavoriteFlow(
                    latitude = latitude,
                    longitude = longitude,
                ).first()

                Triple(weatherApiResult, resolvedCityNameResult, isFavoriteResult)
            }

        val weatherResult = result.first
        val resolvedCityName = result.second
        val isFavorite = result.third

        when (weatherResult) {
            is ApiResult.Success -> {
                val updatedWeather = if (resolvedCityName != null) {
                    weatherResult.value.copy(city = resolvedCityName)
                } else {
                    weatherResult.value
                }

                mutableForecastUiState.value = ForecastUiState.Content(
                    weather = updatedWeather,
                    isRefreshing = false,
                    isFavorite = isFavorite,
                )
            }

            is ApiResult.Failure -> {
                mutableForecastUiState.value = ForecastUiState.Error(
                    apiError = weatherResult.error,
                )
            }
        }
    }
}
