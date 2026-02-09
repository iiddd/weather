package com.iiddd.weather.core.preferences.favorites

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private object FavoritesPreferencesKeys {
    val favoritesListKey = stringPreferencesKey(name = "favorites_list")
}

private const val COORDINATE_TOLERANCE = 0.0001

class DataStoreFavoritesRepository(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) : FavoritesRepository {

    override val favoritesFlow: Flow<List<FavoriteLocation>> =
        dataStore.data.map { preferences ->
            val storedValue: String? = preferences[FavoritesPreferencesKeys.favoritesListKey]
            if (storedValue.isNullOrEmpty()) {
                emptyList()
            } else {
                try {
                    json.decodeFromString<List<FavoriteLocation>>(storedValue)
                } catch (exception: Exception) {
                    emptyList()
                }
            }
        }

    override fun isFavoriteFlow(latitude: Double, longitude: Double): Flow<Boolean> =
        favoritesFlow.map { favorites ->
            favorites.any { favoriteLocation ->
                coordinatesMatch(
                    latitude1 = favoriteLocation.latitude,
                    longitude1 = favoriteLocation.longitude,
                    latitude2 = latitude,
                    longitude2 = longitude,
                )
            }
        }

    override suspend fun addFavorite(favoriteLocation: FavoriteLocation) {
        dataStore.edit { preferences ->
            val currentList = getCurrentList(preferences = preferences)
            val alreadyExists = currentList.any { existingLocation ->
                coordinatesMatch(
                    latitude1 = existingLocation.latitude,
                    longitude1 = existingLocation.longitude,
                    latitude2 = favoriteLocation.latitude,
                    longitude2 = favoriteLocation.longitude,
                ) || existingLocation.cityName.equals(
                    other = favoriteLocation.cityName,
                    ignoreCase = true,
                )
            }

            if (!alreadyExists) {
                val updatedList = currentList + favoriteLocation
                preferences[FavoritesPreferencesKeys.favoritesListKey] = json.encodeToString(updatedList)
            }
        }
    }

    override suspend fun removeFavorite(latitude: Double, longitude: Double) {
        dataStore.edit { preferences ->
            val currentList = getCurrentList(preferences = preferences)
            val updatedList = currentList.filterNot { favoriteLocation ->
                coordinatesMatch(
                    latitude1 = favoriteLocation.latitude,
                    longitude1 = favoriteLocation.longitude,
                    latitude2 = latitude,
                    longitude2 = longitude,
                )
            }
            preferences[FavoritesPreferencesKeys.favoritesListKey] = json.encodeToString(updatedList)
        }
    }

    private fun getCurrentList(preferences: Preferences): List<FavoriteLocation> {
        val storedValue: String? = preferences[FavoritesPreferencesKeys.favoritesListKey]
        return if (storedValue.isNullOrEmpty()) {
            emptyList()
        } else {
            try {
                json.decodeFromString<List<FavoriteLocation>>(storedValue)
            } catch (exception: Exception) {
                emptyList()
            }
        }
    }

    private fun coordinatesMatch(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double,
    ): Boolean {
        return kotlin.math.abs(latitude1 - latitude2) < COORDINATE_TOLERANCE &&
                kotlin.math.abs(longitude1 - longitude2) < COORDINATE_TOLERANCE
    }
}
