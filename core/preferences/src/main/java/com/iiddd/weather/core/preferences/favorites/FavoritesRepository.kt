package com.iiddd.weather.core.preferences.favorites

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    val favoritesFlow: Flow<List<FavoriteLocation>>

    fun isFavoriteFlow(latitude: Double, longitude: Double): Flow<Boolean>

    suspend fun addFavorite(favoriteLocation: FavoriteLocation)

    suspend fun removeFavorite(latitude: Double, longitude: Double)
}
