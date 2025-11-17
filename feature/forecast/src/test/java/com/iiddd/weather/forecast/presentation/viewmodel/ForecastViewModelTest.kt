package com.iiddd.weather.forecast.presentation.viewmodel

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

    private val dispatcherProvider = UnitTestDispatcherProvider()

    private lateinit var repo: WeatherRepository
    private lateinit var viewModel: ForecastViewModel

    @BeforeEach
    fun setUp() {
        repo = mock()
        viewModel = ForecastViewModel(
            weatherRepository = repo,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `loadWeather when repository returns weather successfully`() =
        runTest(dispatcherProvider.dispatcher) {
            val weather = Weather(currentTemp = 10.0, description = "desc")
            whenever(repo.getWeather(latitude = 1.0, longitude = 2.0)).thenReturn(weather)

            viewModel.loadWeather(lat = 1.0, lon = 2.0)

            val state = viewModel.weather.value
            assertEquals(weather, state)
        }

    @Test
    fun `loadWeather when repository throws RuntimeException sets weather to null`() =
        runTest(dispatcherProvider.dispatcher) {
            whenever(
                repo.getWeather(
                    latitude = 1.0,
                    longitude = 2.0
                )
            ).thenThrow(RuntimeException("boom"))

            viewModel.loadWeather(lat = 1.0, lon = 2.0)

            val state = viewModel.weather.value
            assertNull(state)
        }

    @Test
    fun `loadWeather when repository throws IllegalArgumentException sets weather to null`() =
        runTest(dispatcherProvider.dispatcher) {
            whenever(
                repo.getWeather(
                    latitude = 1.0,
                    longitude = 2.0
                )
            ).thenThrow(IllegalArgumentException("bad"))

            viewModel.loadWeather(lat = 1.0, lon = 2.0)

            val state = viewModel.weather.value
            assertNull(state)
        }

    @Test
    fun `loadWeather when city parameter provided sets city on returned weather`() =
        runTest(dispatcherProvider.dispatcher) {
            val repoWeather = Weather(currentTemp = 5.0, description = "sunny")
            whenever(repo.getWeather(latitude = 1.0, longitude = 2.0)).thenReturn(repoWeather)

            viewModel.loadWeather(lat = 1.0, lon = 2.0, city = "ProvidedCity")

            val state = viewModel.weather.value
            assertEquals("ProvidedCity", state?.city)
            assertEquals(repoWeather.currentTemp, state?.currentTemp)
            assertEquals(repoWeather.description, state?.description)
        }

    @Test
    fun `loadWeather when city parameter provided overrides repository city`() =
        runTest(dispatcherProvider.dispatcher) {
            val repoWeather = Weather(currentTemp = 7.0, description = "cloudy", city = "RepoCity")
            whenever(repo.getWeather(latitude = 1.0, longitude = 2.0)).thenReturn(repoWeather)

            viewModel.loadWeather(lat = 1.0, lon = 2.0, city = "ProvidedCity")

            val state = viewModel.weather.value
            assertEquals("ProvidedCity", state?.city)
            assertEquals(repoWeather.currentTemp, state?.currentTemp)
            assertEquals(repoWeather.description, state?.description)
        }
}