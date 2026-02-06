package com.iiddd.weather.forecast.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.testutils.UnitTestDispatcherProvider
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import com.iiddd.weather.location.domain.Coordinates
import com.iiddd.weather.location.domain.GeocodingService
import com.iiddd.weather.location.domain.LocationTracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class ForecastViewModelTest {

    private val dispatcherProvider = UnitTestDispatcherProvider()

    private lateinit var weatherRepository: WeatherRepository
    private lateinit var geocodingService: GeocodingService
    private lateinit var locationTracker: LocationTracker
    private lateinit var forecastViewModel: ForecastViewModel

    @BeforeEach
    fun setUp() {
        weatherRepository = mock()
        geocodingService = mock()
        locationTracker = mock()

        forecastViewModel =
            ForecastViewModel(
                weatherRepository = weatherRepository,
                geocodingService = geocodingService,
                locationTracker = locationTracker,
                dispatcherProvider = dispatcherProvider,
            )
    }

    @Test
    fun `LoadWeatherRequested with coordinates emits Content when repository succeeds`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val weather = Weather(currentTemp = 10, description = "desc")
            whenever(
                weatherRepository.getWeather(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(ApiResult.Success(value = weather))

            whenever(
                geocodingService.reverseGeocode(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(null)

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(
                    latitude = 1.0,
                    longitude = 2.0,
                    useDeviceLocation = false,
                ),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Content)

            val contentState = finalState as ForecastUiState.Content
            assertEquals(weather, contentState.weather)
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
                geocodingService.reverseGeocode(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(null)

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(
                    latitude = 1.0,
                    longitude = 2.0,
                    useDeviceLocation = false,
                ),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Error)

            val errorState = finalState as ForecastUiState.Error
            assertEquals(expectedError, errorState.apiError)
        }

    @Test
    fun `LoadWeatherRequested applies resolved city name to weather when available`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val repositoryWeather = Weather(currentTemp = 5, description = "sunny")
            whenever(
                weatherRepository.getWeather(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(ApiResult.Success(value = repositoryWeather))

            whenever(
                geocodingService.reverseGeocode(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn("ProvidedCity")

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(
                    latitude = 1.0,
                    longitude = 2.0,
                    useDeviceLocation = false,
                ),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Content)

            val contentState = finalState as ForecastUiState.Content
            assertEquals("ProvidedCity", contentState.weather.city)
            assertEquals(repositoryWeather.currentTemp, contentState.weather.currentTemp)
            assertEquals(repositoryWeather.description, contentState.weather.description)
        }

    @Test
    fun `LoadWeatherRequested overrides repository city with resolved city name`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val repositoryWeather = Weather(
                currentTemp = 5,
                description = "sunny",
                city = "RepositoryCity",
            )

            whenever(
                weatherRepository.getWeather(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn(ApiResult.Success(value = repositoryWeather))

            whenever(
                geocodingService.reverseGeocode(
                    latitude = 1.0,
                    longitude = 2.0,
                ),
            ).thenReturn("ProvidedCity")

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(
                    latitude = 1.0,
                    longitude = 2.0,
                    useDeviceLocation = false,
                ),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Content)

            val contentState = finalState as ForecastUiState.Content
            assertEquals("ProvidedCity", contentState.weather.city)
            assertEquals(repositoryWeather.currentTemp, contentState.weather.currentTemp)
            assertEquals(repositoryWeather.description, contentState.weather.description)
        }

    @Test
    fun `LoadWeatherRequested with useDeviceLocation uses current location when available`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(locationTracker.getLastKnownLocation())
                .thenReturn(Coordinates(latitude = 11.0, longitude = 22.0))

            val weather = Weather(currentTemp = 1, description = "ok")
            whenever(
                weatherRepository.getWeather(
                    latitude = 11.0,
                    longitude = 22.0,
                ),
            ).thenReturn(ApiResult.Success(value = weather))

            whenever(
                geocodingService.reverseGeocode(
                    latitude = 11.0,
                    longitude = 22.0,
                ),
            ).thenReturn(null)

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(
                    latitude = null,
                    longitude = null,
                    useDeviceLocation = true,
                ),
            )

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Content)

            val contentState = finalState as ForecastUiState.Content
            assertEquals(weather, contentState.weather)
        }

    @Test
    fun `LoadWeatherRequested with useDeviceLocation emits Error when no location available`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(locationTracker.getLastKnownLocation())
                .thenReturn(null)
            whenever(locationTracker.getCurrentLocationOrNull())
                .thenReturn(null)

            forecastViewModel.onEvent(
                forecastUiEvent = ForecastUiEvent.LoadWeatherRequested(
                    latitude = null,
                    longitude = null,
                    useDeviceLocation = true,
                ),
            )

            advanceUntilIdle()

            val finalState = forecastViewModel.forecastUiState.value
            assertTrue(finalState is ForecastUiState.Error)

            val errorState = finalState as ForecastUiState.Error
            val apiError = errorState.apiError
            assertTrue(apiError is ApiError.Input)
        }
}
