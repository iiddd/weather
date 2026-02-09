package com.iiddd.weather.search.data

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.testutils.UnitTestDispatcherProvider
import com.iiddd.weather.location.domain.GeocodedLocation
import com.iiddd.weather.location.domain.GeocodingService
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SearchRepositoryImplTest {

    private val dispatcherProvider = UnitTestDispatcherProvider()
    private lateinit var geocodingService: GeocodingService
    private lateinit var searchRepository: SearchRepositoryImpl

    @BeforeEach
    fun setUp() {
        geocodingService = mock()
        searchRepository = SearchRepositoryImpl(
            geocodingService = geocodingService,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `searchLocation returns success with mapped locations when geocoding returns results`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val geocodedLocations = listOf(
                GeocodedLocation(
                    latitude = 52.3676,
                    longitude = 4.9041,
                    formattedAddress = "Amsterdam, Netherlands",
                ),
                GeocodedLocation(
                    latitude = 52.0907,
                    longitude = 5.1214,
                    formattedAddress = "Utrecht, Netherlands",
                ),
            )
            whenever(
                geocodingService.forwardGeocode(
                    query = "Amsterdam",
                    maxResults = 5,
                )
            ).thenReturn(geocodedLocations)

            val result = searchRepository.searchLocation(
                query = "Amsterdam",
                maxResults = 5,
            )

            assertTrue(result is ApiResult.Success)
            val successResult = result as ApiResult.Success
            assertEquals(2, successResult.value.size)

            val firstLocation = successResult.value[0]
            assertEquals(52.3676, firstLocation.lat)
            assertEquals(4.9041, firstLocation.lon)
            assertEquals("Amsterdam, Netherlands", firstLocation.name)

            val secondLocation = successResult.value[1]
            assertEquals(52.0907, secondLocation.lat)
            assertEquals(5.1214, secondLocation.lon)
            assertEquals("Utrecht, Netherlands", secondLocation.name)
        }

    @Test
    fun `searchLocation returns input error when geocoding returns empty list`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(
                geocodingService.forwardGeocode(
                    query = "NonExistentPlace",
                    maxResults = 1,
                )
            ).thenReturn(emptyList())

            val result = searchRepository.searchLocation(
                query = "NonExistentPlace",
                maxResults = 1,
            )

            assertTrue(result is ApiResult.Failure)
            val failureResult = result as ApiResult.Failure
            assertTrue(failureResult.error is ApiError.Input)
            assertEquals(
                "No results found for 'NonExistentPlace'",
                (failureResult.error as ApiError.Input).message,
            )
        }

    @Test
    fun `searchLocation returns network error when geocoding throws exception`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(
                geocodingService.forwardGeocode(
                    query = "Test",
                    maxResults = 1,
                )
            ).thenThrow(RuntimeException("Connection failed"))

            val result = searchRepository.searchLocation(
                query = "Test",
                maxResults = 1,
            )

            assertTrue(result is ApiResult.Failure)
            val failureResult = result as ApiResult.Failure
            assertTrue(failureResult.error is ApiError.Network)
            assertEquals(
                "Connection failed",
                (failureResult.error as ApiError.Network).message,
            )
        }

    @Test
    fun `searchLocation returns network error with unknown message when exception has no message`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(
                geocodingService.forwardGeocode(
                    query = "Test",
                    maxResults = 1,
                )
            ).thenThrow(RuntimeException())

            val result = searchRepository.searchLocation(
                query = "Test",
                maxResults = 1,
            )

            assertTrue(result is ApiResult.Failure)
            val failureResult = result as ApiResult.Failure
            assertTrue(failureResult.error is ApiError.Network)
            assertEquals(
                "Unknown error",
                (failureResult.error as ApiError.Network).message,
            )
        }

    @Test
    fun `searchLocation returns success with single location`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val geocodedLocation = GeocodedLocation(
                latitude = 35.6762,
                longitude = 139.6503,
                formattedAddress = "Tokyo, Japan",
            )
            whenever(
                geocodingService.forwardGeocode(
                    query = "Tokyo",
                    maxResults = 1,
                )
            ).thenReturn(listOf(geocodedLocation))

            val result = searchRepository.searchLocation(
                query = "Tokyo",
                maxResults = 1,
            )

            assertTrue(result is ApiResult.Success)
            val successResult = result as ApiResult.Success
            assertEquals(1, successResult.value.size)
            assertEquals("Tokyo, Japan", successResult.value[0].name)
        }
}

