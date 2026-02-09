package com.iiddd.weather.location.data

import com.iiddd.weather.location.data.api.GeocodingApi
import com.iiddd.weather.location.data.dto.AddressComponent
import com.iiddd.weather.location.data.dto.GeocodingResponse
import com.iiddd.weather.location.data.dto.GeocodingResult
import com.iiddd.weather.location.data.dto.Geometry
import com.iiddd.weather.location.data.dto.LocationDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class GoogleGeocodingServiceTest {

    private lateinit var geocodingApi: GeocodingApi
    private lateinit var geocodingService: GoogleGeocodingService

    private val testApiKey = "test_api_key"

    @BeforeEach
    fun setUp() {
        geocodingApi = mock()
        geocodingService = GoogleGeocodingService(
            geocodingApi = geocodingApi,
            apiKey = testApiKey,
        )
    }

    @Test
    fun `forwardGeocode returns empty list when API throws exception`() = runTest {
        whenever(
            geocodingApi.forwardGeocode(
                address = "Test",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenThrow(RuntimeException("Network error"))

        val result = geocodingService.forwardGeocode(
            query = "Test",
            maxResults = 1,
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `forwardGeocode returns empty list when API returns empty results`() = runTest {
        whenever(
            geocodingApi.forwardGeocode(
                address = "Unknown",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = emptyList()))

        val result = geocodingService.forwardGeocode(
            query = "Unknown",
            maxResults = 1,
        )

        assertTrue(result.isEmpty())
    }

    @Test
    fun `forwardGeocode returns mapped locations with city and country`() = runTest {
        val geocodingResult = createGeocodingResult(
            latitude = 52.3676,
            longitude = 4.9041,
            formattedAddress = "Amsterdam, Netherlands",
            locality = "Amsterdam",
            country = "Netherlands",
        )
        whenever(
            geocodingApi.forwardGeocode(
                address = "Amsterdam",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = listOf(geocodingResult)))

        val result = geocodingService.forwardGeocode(
            query = "Amsterdam",
            maxResults = 1,
        )

        assertEquals(1, result.size)
        assertEquals(52.3676, result[0].latitude)
        assertEquals(4.9041, result[0].longitude)
        assertEquals("Amsterdam, Netherlands", result[0].formattedAddress)
    }

    @Test
    fun `forwardGeocode limits results to maxResults`() = runTest {
        val results = (1..5).map { index ->
            createGeocodingResult(
                latitude = 52.0 + index,
                longitude = 4.0 + index,
                formattedAddress = "City $index",
                locality = "City $index",
                country = "Country",
            )
        }
        whenever(
            geocodingApi.forwardGeocode(
                address = "City",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = results))

        val result = geocodingService.forwardGeocode(
            query = "City",
            maxResults = 3,
        )

        assertEquals(3, result.size)
    }

    @Test
    fun `forwardGeocode filters out results without geometry`() = runTest {
        val validResult = createGeocodingResult(
            latitude = 52.3676,
            longitude = 4.9041,
            formattedAddress = "Amsterdam",
            locality = "Amsterdam",
            country = "Netherlands",
        )
        val invalidResult = GeocodingResult(
            formattedAddress = "Invalid",
            geometry = null,
            addressComponents = emptyList(),
        )
        whenever(
            geocodingApi.forwardGeocode(
                address = "Test",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = listOf(invalidResult, validResult)))

        val result = geocodingService.forwardGeocode(
            query = "Test",
            maxResults = 5,
        )

        assertEquals(1, result.size)
        assertEquals("Amsterdam, Netherlands", result[0].formattedAddress)
    }

    @Test
    fun `forwardGeocode uses formatted address when locality and country are missing`() = runTest {
        val geocodingResult = GeocodingResult(
            formattedAddress = "Full Address, Some Place",
            geometry = Geometry(location = LocationDto(lat = 52.0, lng = 4.0)),
            addressComponents = emptyList(),
        )
        whenever(
            geocodingApi.forwardGeocode(
                address = "Test",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = listOf(geocodingResult)))

        val result = geocodingService.forwardGeocode(
            query = "Test",
            maxResults = 1,
        )

        assertEquals(1, result.size)
        assertEquals("Full Address, Some Place", result[0].formattedAddress)
    }

    @Test
    fun `reverseGeocode returns null when API throws exception`() = runTest {
        whenever(
            geocodingApi.reverseGeocode(
                latitudeLongitude = "52.3676,4.9041",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenThrow(RuntimeException("Network error"))

        val result = geocodingService.reverseGeocode(
            latitude = 52.3676,
            longitude = 4.9041,
        )

        assertNull(result)
    }

    @Test
    fun `reverseGeocode returns null when API returns empty results`() = runTest {
        whenever(
            geocodingApi.reverseGeocode(
                latitudeLongitude = "52.3676,4.9041",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = emptyList()))

        val result = geocodingService.reverseGeocode(
            latitude = 52.3676,
            longitude = 4.9041,
        )

        assertNull(result)
    }

    @Test
    fun `reverseGeocode returns city and country when available`() = runTest {
        val geocodingResult = createGeocodingResult(
            latitude = 52.3676,
            longitude = 4.9041,
            formattedAddress = "Full address",
            locality = "Amsterdam",
            country = "Netherlands",
        )
        whenever(
            geocodingApi.reverseGeocode(
                latitudeLongitude = "52.3676,4.9041",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = listOf(geocodingResult)))

        val result = geocodingService.reverseGeocode(
            latitude = 52.3676,
            longitude = 4.9041,
        )

        assertEquals("Amsterdam, Netherlands", result)
    }

    @Test
    fun `reverseGeocode uses administrative area when locality is missing`() = runTest {
        val geocodingResult = createGeocodingResult(
            latitude = 52.0,
            longitude = 4.0,
            formattedAddress = "Full address",
            locality = null,
            administrativeArea = "North Holland",
            country = "Netherlands",
        )
        whenever(
            geocodingApi.reverseGeocode(
                latitudeLongitude = "52.0,4.0",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = listOf(geocodingResult)))

        val result = geocodingService.reverseGeocode(
            latitude = 52.0,
            longitude = 4.0,
        )

        assertEquals("North Holland, Netherlands", result)
    }

    @Test
    fun `reverseGeocode returns formatted address when city and country are missing`() = runTest {
        val geocodingResult = GeocodingResult(
            formattedAddress = "Some Full Address",
            geometry = Geometry(location = LocationDto(lat = 52.0, lng = 4.0)),
            addressComponents = emptyList(),
        )
        whenever(
            geocodingApi.reverseGeocode(
                latitudeLongitude = "52.0,4.0",
                apiKey = testApiKey,
                language = "en",
            )
        ).thenReturn(GeocodingResponse(results = listOf(geocodingResult)))

        val result = geocodingService.reverseGeocode(
            latitude = 52.0,
            longitude = 4.0,
        )

        assertEquals("Some Full Address", result)
    }

    private fun createGeocodingResult(
        latitude: Double,
        longitude: Double,
        formattedAddress: String,
        locality: String? = null,
        administrativeArea: String? = null,
        country: String? = null,
    ): GeocodingResult {
        val addressComponents = mutableListOf<AddressComponent>()

        locality?.let {
            addressComponents.add(
                AddressComponent(
                    longName = it,
                    shortName = it,
                    types = listOf("locality"),
                )
            )
        }

        administrativeArea?.let {
            addressComponents.add(
                AddressComponent(
                    longName = it,
                    shortName = it,
                    types = listOf("administrative_area_level_1"),
                )
            )
        }

        country?.let {
            addressComponents.add(
                AddressComponent(
                    longName = it,
                    shortName = it,
                    types = listOf("country"),
                )
            )
        }

        return GeocodingResult(
            formattedAddress = formattedAddress,
            geometry = Geometry(location = LocationDto(lat = latitude, lng = longitude)),
            addressComponents = addressComponents,
        )
    }
}

