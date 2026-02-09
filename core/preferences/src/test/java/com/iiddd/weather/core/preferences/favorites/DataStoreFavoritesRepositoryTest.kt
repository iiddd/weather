package com.iiddd.weather.core.preferences.favorites

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreFavoritesRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @TempDir
    lateinit var tempDir: File

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var json: Json
    private lateinit var favoritesRepository: DataStoreFavoritesRepository

    private val favoritesListKey = stringPreferencesKey(name = "favorites_list")

    @BeforeEach
    fun setUp() {
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
        ) {
            File(tempDir, "test_favorites.preferences_pb")
        }
        json = Json {
            ignoreUnknownKeys = true
            explicitNulls = false
        }
        favoritesRepository = DataStoreFavoritesRepository(
            dataStore = dataStore,
            json = json,
        )
    }

    @Test
    fun `favoritesFlow returns empty list when preferences is empty`() = testScope.runTest {
        val result = favoritesRepository.favoritesFlow.first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `favoritesFlow returns empty list when preferences contains empty string`() = testScope.runTest {
        dataStore.edit { preferences ->
            preferences[favoritesListKey] = ""
        }

        val result = favoritesRepository.favoritesFlow.first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `favoritesFlow returns parsed favorites when preferences contains valid JSON`() = testScope.runTest {
        val favoritesList = listOf(
            FavoriteLocation(
                id = "1",
                cityName = "Amsterdam",
                latitude = 52.3676,
                longitude = 4.9041,
            ),
            FavoriteLocation(
                id = "2",
                cityName = "Tokyo",
                latitude = 35.6762,
                longitude = 139.6503,
            ),
        )
        val jsonString = json.encodeToString(favoritesList)
        dataStore.edit { preferences ->
            preferences[favoritesListKey] = jsonString
        }

        val result = favoritesRepository.favoritesFlow.first()

        assertEquals(2, result.size)
        assertEquals("Amsterdam", result[0].cityName)
        assertEquals(52.3676, result[0].latitude)
        assertEquals("Tokyo", result[1].cityName)
        assertEquals(35.6762, result[1].latitude)
    }

    @Test
    fun `favoritesFlow returns empty list when preferences contains invalid JSON`() = testScope.runTest {
        dataStore.edit { preferences ->
            preferences[favoritesListKey] = "invalid json {{{"
        }

        val result = favoritesRepository.favoritesFlow.first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `isFavoriteFlow returns true when location exists in favorites`() = testScope.runTest {
        val favoritesList = listOf(
            FavoriteLocation(
                id = "1",
                cityName = "Amsterdam",
                latitude = 52.3676,
                longitude = 4.9041,
            ),
        )
        val jsonString = json.encodeToString(favoritesList)
        dataStore.edit { preferences ->
            preferences[favoritesListKey] = jsonString
        }

        val result = favoritesRepository.isFavoriteFlow(
            latitude = 52.3676,
            longitude = 4.9041,
        ).first()

        assertTrue(result)
    }

    @Test
    fun `isFavoriteFlow returns true when location is within tolerance`() = testScope.runTest {
        val favoritesList = listOf(
            FavoriteLocation(
                id = "1",
                cityName = "Amsterdam",
                latitude = 52.3676,
                longitude = 4.9041,
            ),
        )
        val jsonString = json.encodeToString(favoritesList)
        dataStore.edit { preferences ->
            preferences[favoritesListKey] = jsonString
        }

        val result = favoritesRepository.isFavoriteFlow(
            latitude = 52.36765,
            longitude = 4.90415,
        ).first()

        assertTrue(result)
    }

    @Test
    fun `isFavoriteFlow returns false when location does not exist in favorites`() = testScope.runTest {
        val favoritesList = listOf(
            FavoriteLocation(
                id = "1",
                cityName = "Amsterdam",
                latitude = 52.3676,
                longitude = 4.9041,
            ),
        )
        val jsonString = json.encodeToString(favoritesList)
        dataStore.edit { preferences ->
            preferences[favoritesListKey] = jsonString
        }

        val result = favoritesRepository.isFavoriteFlow(
            latitude = 35.6762,
            longitude = 139.6503,
        ).first()

        assertFalse(result)
    }

    @Test
    fun `isFavoriteFlow returns false when favorites list is empty`() = testScope.runTest {
        val result = favoritesRepository.isFavoriteFlow(
            latitude = 52.3676,
            longitude = 4.9041,
        ).first()

        assertFalse(result)
    }

    @Test
    fun `favoritesFlow returns single favorite correctly`() = testScope.runTest {
        val favoritesList = listOf(
            FavoriteLocation(
                id = "unique-id",
                cityName = "London",
                latitude = 51.5074,
                longitude = -0.1278,
            ),
        )
        val jsonString = json.encodeToString(favoritesList)
        dataStore.edit { preferences ->
            preferences[favoritesListKey] = jsonString
        }

        val result = favoritesRepository.favoritesFlow.first()

        assertEquals(1, result.size)
        assertEquals("unique-id", result[0].id)
        assertEquals("London", result[0].cityName)
        assertEquals(51.5074, result[0].latitude)
        assertEquals(-0.1278, result[0].longitude)
    }

    @Test
    fun `addFavorite adds location to empty favorites list`() = testScope.runTest {
        val favoriteLocation = FavoriteLocation(
            id = "1",
            cityName = "Paris",
            latitude = 48.8566,
            longitude = 2.3522,
        )

        favoritesRepository.addFavorite(favoriteLocation = favoriteLocation)

        val result = favoritesRepository.favoritesFlow.first()
        assertEquals(1, result.size)
        assertEquals("Paris", result[0].cityName)
    }

    @Test
    fun `addFavorite does not add duplicate location`() = testScope.runTest {
        val favoriteLocation = FavoriteLocation(
            id = "1",
            cityName = "Paris",
            latitude = 48.8566,
            longitude = 2.3522,
        )

        favoritesRepository.addFavorite(favoriteLocation = favoriteLocation)
        favoritesRepository.addFavorite(favoriteLocation = favoriteLocation)

        val result = favoritesRepository.favoritesFlow.first()
        assertEquals(1, result.size)
    }

    @Test
    fun `removeFavorite removes location from favorites`() = testScope.runTest {
        val favoriteLocation = FavoriteLocation(
            id = "1",
            cityName = "Berlin",
            latitude = 52.52,
            longitude = 13.405,
        )
        favoritesRepository.addFavorite(favoriteLocation = favoriteLocation)

        favoritesRepository.removeFavorite(
            latitude = 52.52,
            longitude = 13.405,
        )

        val result = favoritesRepository.favoritesFlow.first()
        assertTrue(result.isEmpty())
    }
}
