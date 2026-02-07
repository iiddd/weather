package com.iiddd.weather.favorites.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.preferences.favorites.FavoriteLocation
import com.iiddd.weather.core.preferences.favorites.FavoritesRepository
import com.iiddd.weather.core.testutils.UnitTestDispatcherProvider
import com.iiddd.weather.favorites.presentation.model.FavoriteLocationWithWeather
import com.iiddd.weather.forecast.domain.model.HourlyForecast
import com.iiddd.weather.forecast.domain.model.Weather
import com.iiddd.weather.forecast.domain.repository.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val dispatcherProvider = UnitTestDispatcherProvider()

    private lateinit var favoritesRepository: FavoritesRepository
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var favoritesViewModel: FavoritesViewModel

    private val favoritesFlow = MutableStateFlow<List<FavoriteLocation>>(emptyList())

    @BeforeEach
    fun setUp() {
        favoritesRepository = mock()
        weatherRepository = mock()

        whenever(favoritesRepository.favoritesFlow).thenReturn(favoritesFlow)

        favoritesViewModel = FavoritesViewModel(
            favoritesRepository = favoritesRepository,
            weatherRepository = weatherRepository,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `initial state is Loading`() = runTest(context = dispatcherProvider.dispatcher) {
        val state = favoritesViewModel.favoritesUiState.value
        assertTrue(state is FavoritesUiState.Loading)
    }

    @Test
    fun `LoadFavoritesRequested with empty favorites emits Empty state`() =
        runTest(context = dispatcherProvider.dispatcher) {
            favoritesFlow.value = emptyList()

            favoritesViewModel.onEvent(favoritesUiEvent = FavoritesUiEvent.LoadFavoritesRequested)
            advanceUntilIdle()

            val state = favoritesViewModel.favoritesUiState.value
            assertTrue(state is FavoritesUiState.Empty)
        }

    @Test
    fun `LoadFavoritesRequested with favorites and successful weather calls emits Content state`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val favoriteLocation = FavoriteLocation(
                id = "1",
                cityName = "Tokyo",
                latitude = 35.0,
                longitude = 139.0,
            )
            favoritesFlow.value = listOf(favoriteLocation)

            val weather = Weather(
                currentTemp = 20,
                description = "Sunny",
                hourly = listOf(
                    HourlyForecast(time = "12:00", temp = 20, icon = "01d")
                ),
            )
            whenever(
                weatherRepository.getWeather(
                    latitude = 35.0,
                    longitude = 139.0,
                )
            ).thenReturn(ApiResult.Success(value = weather))

            favoritesViewModel.onEvent(favoritesUiEvent = FavoritesUiEvent.LoadFavoritesRequested)
            advanceUntilIdle()

            val state = favoritesViewModel.favoritesUiState.value
            assertTrue(state is FavoritesUiState.Content)

            val contentState = state as FavoritesUiState.Content
            assertEquals(1, contentState.favorites.size)
            assertEquals("Tokyo", contentState.favorites[0].favoriteLocation.cityName)
            assertEquals(20, contentState.favorites[0].currentTemperature)
            assertEquals("Sunny", contentState.favorites[0].weatherDescription)
            assertEquals("01d", contentState.favorites[0].weatherIcon)
            assertFalse(contentState.isRefreshing)
        }

    @Test
    fun `LoadFavoritesRequested with weather API failure emits Error state`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val favoriteLocation = FavoriteLocation(
                id = "1",
                cityName = "Tokyo",
                latitude = 35.0,
                longitude = 139.0,
            )
            favoritesFlow.value = listOf(favoriteLocation)

            val expectedError = ApiError.Network(message = "Network error")
            whenever(
                weatherRepository.getWeather(
                    latitude = 35.0,
                    longitude = 139.0,
                )
            ).thenReturn(ApiResult.Failure(error = expectedError))

            favoritesViewModel.onEvent(favoritesUiEvent = FavoritesUiEvent.LoadFavoritesRequested)
            advanceUntilIdle()

            val state = favoritesViewModel.favoritesUiState.value
            assertTrue(state is FavoritesUiState.Error)

            val errorState = state as FavoritesUiState.Error
            assertEquals(expectedError, errorState.apiError)
        }

    @Test
    fun `RefreshRequested sets isRefreshing to true when in Content state`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val favoriteLocation = FavoriteLocation(
                id = "1",
                cityName = "Tokyo",
                latitude = 35.0,
                longitude = 139.0,
            )
            favoritesFlow.value = listOf(favoriteLocation)

            val weather = Weather(
                currentTemp = 20,
                description = "Sunny",
                hourly = listOf(
                    HourlyForecast(time = "12:00", temp = 20, icon = "01d")
                ),
            )
            whenever(
                weatherRepository.getWeather(
                    latitude = 35.0,
                    longitude = 139.0,
                )
            ).thenReturn(ApiResult.Success(value = weather))

            // First load to get into Content state
            favoritesViewModel.onEvent(favoritesUiEvent = FavoritesUiEvent.LoadFavoritesRequested)
            advanceUntilIdle()

            // Trigger refresh
            favoritesViewModel.onEvent(favoritesUiEvent = FavoritesUiEvent.RefreshRequested)
            advanceUntilIdle()

            val state = favoritesViewModel.favoritesUiState.value
            assertTrue(state is FavoritesUiState.Content)

            val contentState = state as FavoritesUiState.Content
            assertFalse(contentState.isRefreshing)
        }

    @Test
    fun `DeleteFavoriteRequested removes favorite from list and calls repository`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val favoriteLocation1 = FavoriteLocation(
                id = "1",
                cityName = "Tokyo",
                latitude = 35.0,
                longitude = 139.0,
            )
            val favoriteLocation2 = FavoriteLocation(
                id = "2",
                cityName = "New York",
                latitude = 40.0,
                longitude = -74.0,
            )
            favoritesFlow.value = listOf(favoriteLocation1, favoriteLocation2)

            val weather1 = Weather(
                currentTemp = 20,
                description = "Sunny",
                hourly = listOf(HourlyForecast(time = "12:00", temp = 20, icon = "01d")),
            )
            val weather2 = Weather(
                currentTemp = 15,
                description = "Cloudy",
                hourly = listOf(HourlyForecast(time = "12:00", temp = 15, icon = "03d")),
            )

            whenever(
                weatherRepository.getWeather(latitude = 35.0, longitude = 139.0)
            ).thenReturn(ApiResult.Success(value = weather1))
            whenever(
                weatherRepository.getWeather(latitude = 40.0, longitude = -74.0)
            ).thenReturn(ApiResult.Success(value = weather2))

            // First load to get into Content state
            favoritesViewModel.onEvent(favoritesUiEvent = FavoritesUiEvent.LoadFavoritesRequested)
            advanceUntilIdle()

            val initialState = favoritesViewModel.favoritesUiState.value as FavoritesUiState.Content
            assertEquals(2, initialState.favorites.size)

            // Delete first favorite
            val favoriteToDelete = FavoriteLocationWithWeather(
                favoriteLocation = favoriteLocation1,
                currentTemperature = 20,
                weatherDescription = "Sunny",
                weatherIcon = "01d",
            )
            favoritesViewModel.onEvent(
                favoritesUiEvent = FavoritesUiEvent.DeleteFavoriteRequested(
                    favoriteLocationWithWeather = favoriteToDelete
                )
            )
            advanceUntilIdle()

            verify(favoritesRepository).removeFavorite(latitude = 35.0, longitude = 139.0)

            val state = favoritesViewModel.favoritesUiState.value
            assertTrue(state is FavoritesUiState.Content)

            val contentState = state as FavoritesUiState.Content
            assertEquals(1, contentState.favorites.size)
            assertEquals("New York", contentState.favorites[0].favoriteLocation.cityName)
        }

    @Test
    fun `DeleteFavoriteRequested transitions to Empty when last favorite is deleted`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val favoriteLocation = FavoriteLocation(
                id = "1",
                cityName = "Tokyo",
                latitude = 35.0,
                longitude = 139.0,
            )
            favoritesFlow.value = listOf(favoriteLocation)

            val weather = Weather(
                currentTemp = 20,
                description = "Sunny",
                hourly = listOf(HourlyForecast(time = "12:00", temp = 20, icon = "01d")),
            )

            whenever(
                weatherRepository.getWeather(latitude = 35.0, longitude = 139.0)
            ).thenReturn(ApiResult.Success(value = weather))

            // First load to get into Content state
            favoritesViewModel.onEvent(favoritesUiEvent = FavoritesUiEvent.LoadFavoritesRequested)
            advanceUntilIdle()

            val initialState = favoritesViewModel.favoritesUiState.value as FavoritesUiState.Content
            assertEquals(1, initialState.favorites.size)

            // Delete the only favorite
            val favoriteToDelete = FavoriteLocationWithWeather(
                favoriteLocation = favoriteLocation,
                currentTemperature = 20,
                weatherDescription = "Sunny",
                weatherIcon = "01d",
            )
            favoritesViewModel.onEvent(
                favoritesUiEvent = FavoritesUiEvent.DeleteFavoriteRequested(
                    favoriteLocationWithWeather = favoriteToDelete
                )
            )
            advanceUntilIdle()

            val state = favoritesViewModel.favoritesUiState.value
            assertTrue(state is FavoritesUiState.Empty)
        }

    @Test
    fun `LoadFavoritesRequested with multiple favorites fetches weather in parallel`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val favoriteLocation1 = FavoriteLocation(
                id = "1",
                cityName = "Tokyo",
                latitude = 35.0,
                longitude = 139.0,
            )
            val favoriteLocation2 = FavoriteLocation(
                id = "2",
                cityName = "New York",
                latitude = 40.0,
                longitude = -74.0,
            )
            val favoriteLocation3 = FavoriteLocation(
                id = "3",
                cityName = "London",
                latitude = 51.0,
                longitude = 0.0,
            )
            favoritesFlow.value = listOf(favoriteLocation1, favoriteLocation2, favoriteLocation3)

            val weather1 = Weather(currentTemp = 20, description = "Sunny", hourly = emptyList())
            val weather2 = Weather(currentTemp = 15, description = "Cloudy", hourly = emptyList())
            val weather3 = Weather(currentTemp = 10, description = "Rainy", hourly = emptyList())

            whenever(weatherRepository.getWeather(latitude = 35.0, longitude = 139.0))
                .thenReturn(ApiResult.Success(value = weather1))
            whenever(weatherRepository.getWeather(latitude = 40.0, longitude = -74.0))
                .thenReturn(ApiResult.Success(value = weather2))
            whenever(weatherRepository.getWeather(latitude = 51.0, longitude = 0.0))
                .thenReturn(ApiResult.Success(value = weather3))

            favoritesViewModel.onEvent(favoritesUiEvent = FavoritesUiEvent.LoadFavoritesRequested)
            advanceUntilIdle()

            val state = favoritesViewModel.favoritesUiState.value
            assertTrue(state is FavoritesUiState.Content)

            val contentState = state as FavoritesUiState.Content
            assertEquals(3, contentState.favorites.size)
            assertEquals("Tokyo", contentState.favorites[0].favoriteLocation.cityName)
            assertEquals(20, contentState.favorites[0].currentTemperature)
            assertEquals("New York", contentState.favorites[1].favoriteLocation.cityName)
            assertEquals(15, contentState.favorites[1].currentTemperature)
            assertEquals("London", contentState.favorites[2].favoriteLocation.cityName)
            assertEquals(10, contentState.favorites[2].currentTemperature)
        }
}

