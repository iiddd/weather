package com.iiddd.weather.search.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.iiddd.weather.search.domain.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class SearchUiState(
    val query: String = "",
    val marker: LatLng? = null,
    val markerTitle: String? = null,
    val loading: Boolean = false,
    val error: String? = null
)

class SearchViewModel(
    private val repository: SearchRepository,
//    private val favoritesRepository: FavoritesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState

    fun onQueryChange(q: String) {
        _uiState.value = _uiState.value.copy(query = q, error = null)
    }

    fun search() {
        val q = _uiState.value.query.trim()
        if (q.isEmpty()) return

        _uiState.value = _uiState.value.copy(loading = true, error = null)
        viewModelScope.launch {
            try {
                val list = withContext(Dispatchers.IO) {
                    repository.searchLocation(q, 1)
                }

                val first = list.firstOrNull()
                val marker = first?.let { LatLng(it.lat, it.lon) }
                val title = first?.name ?: q

                _uiState.value = _uiState.value.copy(marker = marker, markerTitle = title, loading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(loading = false, error = e.message ?: "error")
            }
        }
    }

    fun clearMarker() {
        _uiState.value = _uiState.value.copy(marker = null, markerTitle = null)
    }

    fun addFavorite(name: String? = null, lat: Double? = null, lon: Double? = null) {
        val marker = _uiState.value.marker ?: return
        val title = name ?: _uiState.value.markerTitle ?: _uiState.value.query.ifBlank { "Unknown" }
        viewModelScope.launch {
//            favoritesRepository.add(FavoriteCity(title, lat ?: marker.latitude, lon ?: marker.longitude))
        }
    }
}