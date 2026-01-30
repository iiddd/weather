package com.iiddd.weather.search.presentation.viewmodel

import com.google.android.gms.maps.model.LatLng

data class SearchUiState(
    val query: String = "",
    val marker: LatLng? = null,
    val markerTitle: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
