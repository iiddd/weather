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

    private var latestRequestedLatitude: Double? = null
    private var latestRequestedLongitude: Double? = null

    fun onEvent(forecastUiEvent: ForecastUiEvent) {
        when (forecastUiEvent) {
            is ForecastUiEvent.LoadWeatherRequested -> {
                val resolvedLatitude = forecastUiEvent.latitude ?: latestRequestedLatitude
                val resolvedLongitude = forecastUiEvent.longitude ?: latestRequestedLongitude

                loadWeatherForCoordinatesOrCurrentLocation(
                    latitude = resolvedLatitude,
                    longitude = resolvedLongitude,
                )
            }

            ForecastUiEvent.RefreshRequested -> {
                loadWeatherForCoordinatesOrCurrentLocation(
                    latitude = latestRequestedLatitude,
                    longitude = latestRequestedLongitude,
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
        latitude: Double?,
        longitude: Double?,
    ) {
        viewModelScope.launch(context = dispatcherProvider.main) {
            mutableForecastUiState.value = ForecastUiState.Loading

            val coordinatesResult: ApiResult<Coordinates> = resolveCoordinates(
                latitude = latitude,
                longitude = longitude,
            )

            when (coordinatesResult) {
                is ApiResult.Success -> {
                    latestRequestedLatitude = coordinatesResult.value.latitude
                    latestRequestedLongitude = coordinatesResult.value.longitude

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
    ): ApiResult<Coordinates> = withContext(context = dispatcherProvider.io) {
        if (latitude != null && longitude != null) {
            ApiResult.Success(
                value = Coordinates(
                    latitude = latitude,
                    longitude = longitude,
                )
            )
        } else {
            val lastKnownCoordinate = locationTracker.getLastKnownLocation()
            if (lastKnownCoordinate != null) {
                ApiResult.Success(value = lastKnownCoordinate)
            } else {
                ApiResult.Failure(
                    error = ApiError.Input(
                        message = "Location is unavailable. Please enable location services and grant location permission.",
                    )
                )
            }
        }
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