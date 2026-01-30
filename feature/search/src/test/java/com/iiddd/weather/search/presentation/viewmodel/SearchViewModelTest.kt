package com.iiddd.weather.search.presentation.viewmodel

import com.iiddd.weather.core.network.ApiError
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.testutils.UnitTestDispatcherProvider
import com.iiddd.weather.search.domain.Location
import com.iiddd.weather.search.domain.SearchRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class SearchViewModelTest {

    private val dispatcherProvider = UnitTestDispatcherProvider()

    private lateinit var searchRepository: SearchRepository
    private lateinit var searchViewModel: SearchViewModel

    @BeforeEach
    fun setUp() {
        searchRepository = mock()
        searchViewModel = SearchViewModel(
            searchRepository = searchRepository,
            dispatcherProvider = dispatcherProvider,
        )
    }

    @Test
    fun `on query change updates query`() = runTest(context = dispatcherProvider.dispatcher) {
        searchViewModel.onQueryChange(query = "hello")

        val state = searchViewModel.searchUiState.value
        assertEquals("hello", state.query)
        assertNull(state.errorMessage)
        assertFalse(state.isLoading)
        assertNull(state.marker)
        assertNull(state.markerTitle)
    }

    @Test
    fun `onSearch with empty results updates marker title and clears loading`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(
                searchRepository.searchLocation(
                    query = "q",
                    maxResults = 1,
                )
            ).thenReturn(
                ApiResult.Success(value = emptyList())
            )

            searchViewModel.onQueryChange(query = "q")
            searchViewModel.onSearch()

            val state = searchViewModel.searchUiState.value
            assertEquals("q", state.query)
            assertNull(state.marker)
            assertEquals("q", state.markerTitle)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
        }

    @Test
    fun `onSearch when repository returns ApiError sets error message and clears loading`() =
        runTest(context = dispatcherProvider.dispatcher) {
            whenever(
                searchRepository.searchLocation(
                    query = "boom",
                    maxResults = 1,
                )
            ).thenReturn(
                ApiResult.Failure(error = ApiError.Network(message = "boom"))
            )

            searchViewModel.onQueryChange(query = "boom")
            searchViewModel.onSearch()

            val state = searchViewModel.searchUiState.value
            assertEquals("boom", state.query)
            assertNull(state.marker)
            assertNull(state.markerTitle)
            assertFalse(state.isLoading)
            assertEquals("Network error. Check your connection and retry.", state.errorMessage)
        }

    @Test
    fun `onSearch when repository returns result sets marker and title`() =
        runTest(context = dispatcherProvider.dispatcher) {
            val location = mock<Location> {
                on { lat } doReturn 10.0
                on { lon } doReturn 20.0
                on { name } doReturn "Place"
            }

            whenever(
                searchRepository.searchLocation(
                    query = "q",
                    maxResults = 1,
                )
            ).thenReturn(
                ApiResult.Success(value = listOf(location))
            )

            searchViewModel.onQueryChange(query = "q")
            searchViewModel.onSearch()

            val state = searchViewModel.searchUiState.value
            assertEquals("q", state.query)
            assertNotNull(state.marker)

            val marker = requireNotNull(state.marker)
            assertEquals(10.0, marker.latitude)
            assertEquals(20.0, marker.longitude)
            assertEquals("Place", state.markerTitle)
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
        }

    @Test
    fun `onClearMarker clears marker and title`() = runTest(context = dispatcherProvider.dispatcher) {
        val location = mock<Location> {
            on { lat } doReturn 10.0
            on { lon } doReturn 20.0
            on { name } doReturn "Place"
        }

        whenever(
            searchRepository.searchLocation(
                query = "q",
                maxResults = 1,
            )
        ).thenReturn(
            ApiResult.Success(value = listOf(location))
        )

        searchViewModel.onQueryChange(query = "q")
        searchViewModel.onSearch()

        val before = searchViewModel.searchUiState.value
        assertNotNull(before.marker)
        assertEquals("Place", before.markerTitle)

        searchViewModel.onClearMarker()

        val state = searchViewModel.searchUiState.value
        assertNull(state.marker)
        assertNull(state.markerTitle)
        assertEquals("q", state.query)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }
}