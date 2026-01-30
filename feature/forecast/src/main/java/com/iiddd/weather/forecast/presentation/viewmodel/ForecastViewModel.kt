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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

            val coordinatesResult: ApiResult<Coordinates> = resolveCoordinates(
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

    private suspend fun resolveCoordinates(
        latitude: Double?,
        longitude: Double?,
        useDeviceLocation: Boolean,
    ): ApiResult<Coordinates> = withContext(context = dispatcherProvider.io) {
        if (latitude != null && longitude != null) {
            return@withContext ApiResult.Success(
                value = Coordinates(
                    latitude = latitude,
                    longitude = longitude,
                )
            )
        }

        if (!useDeviceLocation) {
            return@withContext ApiResult.Failure(
                error = ApiError.Input(
                    message = "No coordinates provided and device location not requested.",
                )
            )
        }

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

                mutableForecastUiState.value = ForecastUiState.Content(
                    weather = updatedWeather,
                    isRefreshing = false,
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
