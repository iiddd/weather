package com.iiddd.weather.forecast.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.testutils.UnitTestDispatcherProvider
import com.iiddd.weather.forecast.domain.location.CityNameResolver
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import com.iiddd.weather.location.domain.Coordinates
import com.iiddd.weather.location.domain.LocationTracker
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ForecastViewModelTest {

    private val dispatcherProvider = UnitTestDispatcherProvider()

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var cityNameResolver: CityNameResolver
    private lateinit var locationTracker: LocationTracker

    private lateinit var forecastViewModel: ForecastViewModel

    @BeforeEach
    fun setUp() {
        weatherRepository = mock()
        cityNameResolver = mock()
        locationTracker = mock()

        forecastViewModel =
            ForecastViewModel(
                weatherRepository = weatherRepository,
                cityNameResolver = cityNameResolver,
                locationTracker = locationTracker,
                dispatcherProvider = dispatcherProvider,
            )
    }

    @Test
    fun `LoadWeatherRequested with coordinates emits Content when repository succeeds`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val weather = Weather(currentTemp = 10.0, description = "desc")
            whenever(
                weatherRepository.getWeather(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(ApiResult.Success(value = weather))

            whenever(
                cityNameResolver.resolveCityName(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(null)

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(latitude = 1.0, longitude = 2.0),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Content)

            val contentState = finalState as ForecastUiState.Content
            assertEquals(weather, contentState.detailedWeatherContent.weather)
        }

    @Test
    fun `LoadWeatherRequested with coordinates emits Error when repository fails`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val expectedError = ApiError.Network(message = "boom")
            whenever(
                weatherRepository.getWeather(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(ApiResult.Failure(error = expectedError))

            whenever(
                cityNameResolver.resolveCityName(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(null)

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(latitude = 1.0, longitude = 2.0),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Error)

            val errorState = finalState as ForecastUiState.Error
            assertEquals(expectedError, errorState.apiError)
        }

    @Test
    fun `LoadWeatherRequested applies resolved city name to weather when available`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val repositoryWeather = Weather(currentTemp = 5.0, description = "sunny")
            whenever(
                weatherRepository.getWeather(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(ApiResult.Success(value = repositoryWeather))

            whenever(
                cityNameResolver.resolveCityName(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn("ProvidedCity")

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(latitude = 1.0, longitude = 2.0),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Content)

            val contentState = finalState as ForecastUiState.Content
            assertEquals("ProvidedCity", contentState.detailedWeatherContent.weather.city)
            assertEquals(repositoryWeather.currentTemp, contentState.detailedWeatherContent.weather.currentTemp)
            assertEquals(repositoryWeather.description, contentState.detailedWeatherContent.weather.description)
        }

    @Test
    fun `LoadWeatherRequested overrides repository city with resolved city name`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val repositoryWeather =
                Weather(
                    currentTemp = 7.0,
                    description = "cloudy",
                    city = "RepositoryCity",
                )

            whenever(
                weatherRepository.getWeather(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(ApiResult.Success(value = repositoryWeather))

            whenever(
                cityNameResolver.resolveCityName(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn("ProvidedCity")

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(latitude = 1.0, longitude = 2.0),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Content)

            val contentState = finalState as ForecastUiState.Content
            assertEquals("ProvidedCity", contentState.detailedWeatherContent.weather.city)
            assertEquals(repositoryWeather.currentTemp, contentState.detailedWeatherContent.weather.currentTemp)
            assertEquals(repositoryWeather.description, contentState.detailedWeatherContent.weather.description)
        }

    @Test
    fun `LoadWeatherRequested without coordinates uses current location when available`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(locationTracker.getCurrentLocationOrNull())
                .thenReturn(Coordinates(latitude = 11.0, longitude = 22.0))

            whenever(locationTracker.getLastKnownLocation())
                .thenReturn(null)

            val weather = Weather(currentTemp = 1.0, description = "ok")
            whenever(
                weatherRepository.getWeather(
                    latitude = 11.0,
                    longitude = 22.0,
                ),
            ).thenReturn(ApiResult.Success(value = weather))

            whenever(
                cityNameResolver.resolveCityName(
                    latitude = 11.0,
                    longitude = 22.0,
                ),
            ).thenReturn(null)

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(latitude = null, longitude = null),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Content)

            val contentState = finalState as ForecastUiState.Content
            assertEquals(weather, contentState.detailedWeatherContent.weather)
        }

    @Test
    fun `LoadWeatherRequested without coordinates emits Error when no location available`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(locationTracker.getCurrentLocationOrNull())
                .thenReturn(null)

            whenever(locationTracker.getLastKnownLocation())
                .thenReturn(null)

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(latitude = null, longitude = null),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Error)

            val errorState = finalState as ForecastUiState.Error
            val apiError = errorState.apiError
            assertTrue(apiError is ApiError.Input)
        }
}
