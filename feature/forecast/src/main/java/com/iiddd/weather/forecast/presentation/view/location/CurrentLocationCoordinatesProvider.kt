package com.iiddd.weather.forecast.presentation.view.location

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.iiddd.weather.location.data.FusedLocationTracker

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

class CurrentLocationCoordinatesProvider internal constructor(
    private val getLastKnownCoordinatesOrNull: suspend () -> Coordinates?
) {
    suspend fun getCurrentCoordinatesOrNull(): Coordinates? {
        return getLastKnownCoordinatesOrNull()
    }
}

@Composable
fun rememberCurrentLocationCoordinatesProvider(): CurrentLocationCoordinatesProvider {
    val context = LocalContext.current
    val fusedLocationTracker = remember { FusedLocationTracker(context = context) }

    return remember {
        CurrentLocationCoordinatesProvider(
            getLastKnownCoordinatesOrNull = {
                val location = fusedLocationTracker.getLastKnownLocation()
                if (location == null) {
                    null
                } else {
                    Coordinates(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                }
            }
        )
    }
}