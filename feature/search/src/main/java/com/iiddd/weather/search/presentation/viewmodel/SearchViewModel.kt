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

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : ViewModel() {

    private val mutableSearchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = mutableSearchUiState

    fun onQueryChange(query: String) {
        mutableSearchUiState.value = mutableSearchUiState.value.copy(
            query = query,
            errorMessage = null,
        )
    }

    fun onSearch() {
        val searchQuery = mutableSearchUiState.value.query.trim()
        if (searchQuery.isEmpty()) return

        mutableSearchUiState.value = mutableSearchUiState.value.copy(
            isLoading = true,
            errorMessage = null,
        )

        viewModelScope.launch(context = dispatcherProvider.main) {
            val result: ApiResult<List<Location>> = searchRepository.searchLocation(
                query = searchQuery,
                maxResults = 1,
            )

            when (result) {
                is ApiResult.Success -> {
                    val firstLocation = result.value.firstOrNull()

                    val marker = firstLocation?.let { location: Location ->
                        LatLng(location.lat, location.lon)
                    }

                    val markerTitle = firstLocation?.name ?: searchQuery

                    mutableSearchUiState.value = mutableSearchUiState.value.copy(
                        marker = marker,
                        markerTitle = markerTitle,
                        isLoading = false,
                        errorMessage = null,
                    )
                }

                is ApiResult.Failure -> {
                    mutableSearchUiState.value = mutableSearchUiState.value.copy(
                        isLoading = false,
                        errorMessage = result.error.toUiMessage(),
                    )
                }
            }
        }
    }

    fun onRetrySearch() {
        onSearch()
    }

    fun onClearMarker() {
        mutableSearchUiState.value = mutableSearchUiState.value.copy(
            marker = null,
            markerTitle = null,
        )
    }

    fun onSetMarkerAtCurrentLocation(
        latitude: Double,
        longitude: Double,
        locationTitle: String,
    ) {
        mutableSearchUiState.value = mutableSearchUiState.value.copy(
            marker = LatLng(latitude, longitude),
            markerTitle = locationTitle,
            isLoading = false,
            errorMessage = null,
        )
    }
}