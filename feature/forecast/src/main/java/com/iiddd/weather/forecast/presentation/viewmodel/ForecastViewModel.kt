package com.iiddd.weather.forecast.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.forecast.domain.location.CityNameResolver
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import com.iiddd.weather.location.domain.Coordinates
import com.iiddd.weather.location.domain.LocationTracker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForecastViewModel(
    private val weatherRepository: WeatherRepository,
    private val cityNameResolver: CityNameResolver,
    private val locationTracker: LocationTracker,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : ViewModel() {

    private val mutableForecastUiState: MutableStateFlow<ForecastUiState> =
        MutableStateFlow(value = ForecastUiState.Loading)

    val forecastUiState: StateFlow<ForecastUiState> =
        mutableForecastUiState.asStateFlow()

    private var latestLoadedCoordinates: Coordinates? = null
    private var isUsingDeviceLocation: Boolean = false

    fun onEvent(forecastUiEvent: ForecastUiEvent) {
        when (forecastUiEvent) {
            is ForecastUiEvent.LoadWeatherRequested -> {
                val requestedCoordinates = if (forecastUiEvent.latitude != null && forecastUiEvent.longitude != null) {
                    Coordinates(
                        latitude = forecastUiEvent.latitude,
                        longitude = forecastUiEvent.longitude,
                    )
                } else {
                    null
                }

                val isDeviceLocationRequest = requestedCoordinates == null
                val hasContent = mutableForecastUiState.value is ForecastUiState.Content

                val alreadyLoadedForSameRequest = when {
                    requestedCoordinates != null && hasContent -> {
                        requestedCoordinates == latestLoadedCoordinates
                    }
                    isDeviceLocationRequest && isUsingDeviceLocation && hasContent -> {
                        true
                    }
                    else -> false
                }

                if (alreadyLoadedForSameRequest) {
                    return
                }

                loadWeatherForCoordinatesOrCurrentLocation(
                    coordinates = requestedCoordinates,
                    forceRefresh = false,
                )
            }

            ForecastUiEvent.RefreshRequested -> {
                val coordinatesToRefresh = if (isUsingDeviceLocation) null else latestLoadedCoordinates
                loadWeatherForCoordinatesOrCurrentLocation(
                    coordinates = coordinatesToRefresh,
                    forceRefresh = true,
                )
            }

            ForecastUiEvent.ErrorDismissed -> {
                if (mutableForecastUiState.value is ForecastUiState.Error) {
                    mutableForecastUiState.value = ForecastUiState.Loading
                }
            }
        }
    }

    private fun loadWeatherForCoordinatesOrCurrentLocation(
        coordinates: Coordinates?,
        forceRefresh: Boolean,
    ) {
        viewModelScope.launch(context = dispatcherProvider.main) {
            if (!forceRefresh || mutableForecastUiState.value !is ForecastUiState.Content) {
                mutableForecastUiState.value = ForecastUiState.Loading
            }

            val coordinatesResult: ApiResult<Coordinates> = resolveCoordinates(coordinates = coordinates)

            when (coordinatesResult) {
                is ApiResult.Success -> {
                    val resolvedCoordinates = coordinatesResult.value

                    isUsingDeviceLocation = coordinates == null

                    loadWeatherAndCityNameAndEmitState(
                        latitude = resolvedCoordinates.latitude,
                        longitude = resolvedCoordinates.longitude,
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

    private suspend fun resolveCoordinates(
        coordinates: Coordinates?,
    ): ApiResult<Coordinates> = withContext(context = dispatcherProvider.io) {
        if (coordinates != null) {
            return@withContext ApiResult.Success(value = coordinates)
        }

        // Retry getting location with delays - location may not be immediately available
        repeat(times = LOCATION_RETRY_COUNT) { attempt ->
            val lastKnownCoordinate = locationTracker.getLastKnownLocation()
            if (lastKnownCoordinate != null) {
                return@withContext ApiResult.Success(value = lastKnownCoordinate)
            }

            if (attempt < LOCATION_RETRY_COUNT - 1) {
                delay(timeMillis = LOCATION_RETRY_DELAY_MILLISECONDS)
            }
        }

        ApiResult.Failure(
            error = ApiError.Input(
                message = "Location is unavailable. Please enable location services and grant location permission.",
            )
        )
    }

    private suspend fun loadWeatherAndCityNameAndEmitState(
        latitude: Double,
        longitude: Double,
    ) {
        val (weatherResult, resolvedCityName) =
            withContext(context = dispatcherProvider.io) {
                val weatherApiResult = weatherRepository.getWeather(
                    latitude = latitude,
                    longitude = longitude,
                )

                val resolvedCityNameResult = cityNameResolver.resolveCityName(
                    latitude = latitude,
                    longitude = longitude,
                )

                weatherApiResult to resolvedCityNameResult
            }

        when (weatherResult) {
            is ApiResult.Success -> {
                val updatedWeather = if (resolvedCityName != null) {
                    weatherResult.value.copy(city = resolvedCityName)
                } else {
                    weatherResult.value
                }

                latestLoadedCoordinates = Coordinates(
                    latitude = latitude,
                    longitude = longitude,
                )

                mutableForecastUiState.value = ForecastUiState.Content(
                    weather = updatedWeather,
                )
            }

            is ApiResult.Failure -> {
                mutableForecastUiState.value = ForecastUiState.Error(
                    apiError = weatherResult.error,
                )
            }
        }
    }

    private companion object {
        const val LOCATION_RETRY_COUNT = 3
        const val LOCATION_RETRY_DELAY_MILLISECONDS = 500L
    }
}