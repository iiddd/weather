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
    val loading: Boolean = false,
    val error: String? = null
)

class SearchViewModel(private val repository: SearchRepository) : ViewModel() {
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

                val marker = list.firstOrNull()?.let { LatLng(it.lat, it.lon) }

                _uiState.value = _uiState.value.copy(marker = marker, loading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(loading = false, error = e.message ?: "error")
            }
        }
    }

    fun clearMarker() {
        _uiState.value = _uiState.value.copy(marker = null)
    }
}