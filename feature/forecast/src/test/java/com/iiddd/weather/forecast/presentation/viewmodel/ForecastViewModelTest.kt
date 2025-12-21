package com.iiddd.weather.forecast.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.testutils.UnitTestDispatcherProvider
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class ForecastViewModelTest {

    private lateinit var repository: WeatherRepository
    private val dispatcherProvider = UnitTestDispatcherProvider()

    @BeforeEach
    fun setUp() {
        repository = mock()
    }

    @Test
    fun `loadWeather when repository returns weather successfully`() = runTest(dispatcherProvider.dispatcher) {
        val viewModel = ForecastViewModel(
            weatherRepository = repository,
            dispatcherProvider = dispatcherProvider
        )
        val weather = Weather(currentTemp = 10.0, description = "desc")
        whenever(repository.getWeather(latitude = 1.0, longitude = 2.0))
            .thenReturn(ApiResult.Success(weather))

        viewModel.loadWeather(lat = 1.0, lon = 2.0)

        assertEquals(weather, viewModel.weather.value)
    }

    @Test
    fun `loadWeather when repository returns failure sets weather to null`() = runTest(dispatcherProvider.dispatcher) {
        val viewModel = ForecastViewModel(
            weatherRepository = repository,
            dispatcherProvider = dispatcherProvider
        )
        whenever(repository.getWeather(latitude = 1.0, longitude = 2.0))
            .thenReturn(ApiResult.Failure(ApiError.Network("boom")))

        viewModel.loadWeather(lat = 1.0, lon = 2.0)

        assertNull(viewModel.weather.value)
    }

    @Test
    fun `loadWeather when city parameter provided sets city on returned weather`() = runTest(dispatcherProvider.dispatcher) {
        val viewModel = ForecastViewModel(
            weatherRepository = repository,
            dispatcherProvider = dispatcherProvider
        )
        val repoWeather = Weather(currentTemp = 5.0, description = "sunny")
        whenever(repository.getWeather(latitude = 1.0, longitude = 2.0))
            .thenReturn(ApiResult.Success(repoWeather))

        viewModel.loadWeather(lat = 1.0, lon = 2.0, city = "ProvidedCity")

        val state = viewModel.weather.value
        assertEquals("ProvidedCity", state?.city)
        assertEquals(repoWeather.currentTemp, state?.currentTemp)
        assertEquals(repoWeather.description, state?.description)
    }

    @Test
    fun `loadWeather when city parameter provided overrides repository city`() = runTest(dispatcherProvider.dispatcher) {
        val viewModel = ForecastViewModel(
            weatherRepository = repository,
            dispatcherProvider = dispatcherProvider
        )
        val repoWeather = Weather(currentTemp = 7.0, description = "cloudy", city = "RepoCity")
        whenever(repository.getWeather(latitude = 1.0, longitude = 2.0))
            .thenReturn(ApiResult.Success(repoWeather))

        viewModel.loadWeather(lat = 1.0, lon = 2.0, city = "ProvidedCity")

        val state = viewModel.weather.value
        assertEquals("ProvidedCity", state?.city)
        assertEquals(repoWeather.currentTemp, state?.currentTemp)
        assertEquals(repoWeather.description, state?.description)
    }
}