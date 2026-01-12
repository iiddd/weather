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

    private lateinit var repository: SearchRepository
    private lateinit var viewModel: SearchViewModel

    @BeforeEach
    fun setUp() {
        repository = mock()
        viewModel = SearchViewModel(
            repository = repository,
            dispatcherProvider = dispatcherProvider
        )
    }

    @Test
    fun `on query change updates query`() = runTest(dispatcherProvider.dispatcher) {
        viewModel.onQueryChange(query = "hello")

        val state = viewModel.uiState.value
        assertEquals("hello", state.query)
        assertNull(state.error)
        assertFalse(state.loading)
        assertNull(state.marker)
        assertNull(state.markerTitle)
    }

    @Test
    fun `search with empty results updates marker title and clears loading`() =
        runTest(dispatcherProvider.dispatcher) {
            whenever(
                repository.searchLocation(
                    query = "q",
                    maxResults = 1
                )
            ).thenReturn(
                ApiResult.Success(value = emptyList())
            )

            viewModel.onQueryChange(query = "q")
            viewModel.search()

            val state = viewModel.uiState.value
            assertEquals("q", state.query)
            assertNull(state.marker)
            assertEquals("q", state.markerTitle)
            assertFalse(state.loading)
            assertNull(state.error)
        }

    @Test
    fun `search when repository returns ApiError sets error message and clears loading`() =
        runTest(dispatcherProvider.dispatcher) {
            whenever(
                repository.searchLocation(
                    query = "boom",
                    maxResults = 1
                )
            ).thenReturn(
                ApiResult.Failure(error = ApiError.Network(message = "boom"))
            )

            viewModel.onQueryChange(query = "boom")
            viewModel.search()

            val state = viewModel.uiState.value
            assertEquals("boom", state.query)
            assertNull(state.marker)
            assertNull(state.markerTitle)
            assertFalse(state.loading)
            assertEquals("Network error. Check your connection and retry.", state.error)
        }

    @Test
    fun `search when repository returns result sets marker and title`() =
        runTest(dispatcherProvider.dispatcher) {
            val location = mock<Location> {
                on { lat } doReturn 10.0
                on { lon } doReturn 20.0
                on { name } doReturn "Place"
            }

            whenever(
                repository.searchLocation(
                    query = "q",
                    maxResults = 1
                )
            ).thenReturn(
                ApiResult.Success(value = listOf(location))
            )

            viewModel.onQueryChange(query = "q")
            viewModel.search()

            val state = viewModel.uiState.value
            assertEquals("q", state.query)
            assertNotNull(state.marker)

            val marker = requireNotNull(state.marker)
            assertEquals(10.0, marker.latitude)
            assertEquals(20.0, marker.longitude)
            assertEquals("Place", state.markerTitle)
            assertFalse(state.loading)
            assertNull(state.error)
        }

    @Test
    fun `clear marker clears marker and title`() = runTest(dispatcherProvider.dispatcher) {
        val location = mock<Location> {
            on { lat } doReturn 10.0
            on { lon } doReturn 20.0
            on { name } doReturn "Place"
        }

        whenever(
            repository.searchLocation(
                query = "q",
                maxResults = 1
            )
        ).thenReturn(
            ApiResult.Success(value = listOf(location))
        )

        viewModel.onQueryChange(query = "q")
        viewModel.search()

        val before = viewModel.uiState.value
        assertNotNull(before.marker)
        assertEquals("Place", before.markerTitle)

        viewModel.clearMarker()

        val state = viewModel.uiState.value
        assertNull(state.marker)
        assertNull(state.markerTitle)
        assertEquals("q", state.query)
        assertFalse(state.loading)
        assertNull(state.error)
    }
}