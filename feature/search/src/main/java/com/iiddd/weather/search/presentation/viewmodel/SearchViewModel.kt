package com.iiddd.weather.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.iiddd.weather.core.network.ApiResult
import com.iiddd.weather.core.network.toUiMessage
import com.iiddd.weather.core.utils.coroutines.DefaultDispatcherProvider
import com.iiddd.weather.core.utils.coroutines.DispatcherProvider
import com.iiddd.weather.search.domain.Location
import com.iiddd.weather.search.domain.SearchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val marker: LatLng? = null,
    val markerTitle: String? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class SearchViewModel(
    private val repository: SearchRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    private val mutableUiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = mutableUiState

    fun onQueryChange(query: String) {
        mutableUiState.value = mutableUiState.value.copy(
            query = query,
            error = null
        )
    }

    fun search() {
        val searchQuery = mutableUiState.value.query.trim()
        if (searchQuery.isEmpty()) return

        mutableUiState.value = mutableUiState.value.copy(
            loading = true,
            error = null
        )

        viewModelScope.launch(context = dispatcherProvider.main) {
            val result: ApiResult<List<Location>> = repository.searchLocation(
                query = searchQuery,
                maxResults = 1
            )

            when (result) {
                is ApiResult.Success -> {
                    val firstLocation = result.value.firstOrNull()

                    val marker = firstLocation?.let { location: Location ->
                        LatLng(location.lat, location.lon)
                    }

                    val markerTitle = firstLocation?.name ?: searchQuery

                    mutableUiState.value = mutableUiState.value.copy(
                        marker = marker,
                        markerTitle = markerTitle,
                        loading = false,
                        error = null
                    )
                }

                is ApiResult.Failure -> {
                    mutableUiState.value = mutableUiState.value.copy(
                        loading = false,
                        error = result.error.toUiMessage()
                    )
                }
            }
        }
    }

    fun retrySearch() {
        search()
    }

    fun clearMarker() {
        mutableUiState.value = mutableUiState.value.copy(
            marker = null,
            markerTitle = null
        )
    }
}