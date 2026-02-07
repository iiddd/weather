package com.iiddd.weather.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.serialization.json.Json

@Stable
class NavigationBackStack private constructor(
    initialEntries: List<Destination>,
) {
    private val _entries: SnapshotStateList<Destination> =
        mutableStateListOf<Destination>().apply { addAll(initialEntries) }

    val entries: SnapshotStateList<Destination> = _entries
    val current: Destination get() = _entries.last()

    fun push(destination: Destination) {
        _entries += destination
    }

    fun replaceCurrent(destination: Destination) {
        _entries[_entries.lastIndex] = destination
    }

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun create(startDestination: Destination): NavigationBackStack {
            return NavigationBackStack(initialEntries = listOf(startDestination))
        }

        val Saver: Saver<NavigationBackStack, List<String>> = Saver(
            save = { backStack ->
                backStack._entries.map { destination ->
                    json.encodeToString(Destination.serializer(), destination)
                }
            },
            restore = { serializedEntries ->
                val destinations = serializedEntries.map { serializedDestination ->
                    json.decodeFromString(Destination.serializer(), serializedDestination)
                }
                NavigationBackStack(initialEntries = destinations)
            }
        )
    }
}

@Composable
fun rememberNavigationBackStack(
    startDestination: Destination = Destination.Weather(),
): NavigationBackStack {
    return rememberSaveable(saver = NavigationBackStack.Saver) {
        NavigationBackStack.create(startDestination = startDestination)
    }
}