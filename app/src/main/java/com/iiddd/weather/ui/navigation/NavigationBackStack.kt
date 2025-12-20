package com.iiddd.weather.ui.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

class NavigationBackStack(
    startDestination: Destination
) {
    private val _entries: SnapshotStateList<Destination> =
        mutableStateListOf(startDestination)

    val entries: SnapshotStateList<Destination> = _entries
    val current: Destination get() = _entries.last()

    fun push(destination: Destination) {
        _entries += destination
    }

    fun replaceCurrent(destination: Destination) {
        _entries[_entries.lastIndex] = destination
    }
}