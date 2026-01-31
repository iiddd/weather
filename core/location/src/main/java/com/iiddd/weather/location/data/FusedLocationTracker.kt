package com.iiddd.weather.location.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.iiddd.weather.location.domain.Coordinates
import com.iiddd.weather.location.domain.LocationTracker
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class FusedLocationTracker(
    private val applicationContext: Context,
    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(applicationContext),
    private val timeoutMilliseconds: Long = 10_000L,
    private val locationPriority: Int = Priority.PRIORITY_HIGH_ACCURACY,
    private val maxLocationAgeMilliseconds: Long = 60_000L,
) : LocationTracker {

    override suspend fun getLastKnownLocation(): Coordinates? {
        if (!hasLocationPermission()) return null

        return withTimeoutOrNull(timeMillis = timeoutMilliseconds) {
            val lastLocation = lastLocationOrNull()
            if (lastLocation != null && isLocationFresh(location = lastLocation)) {
                return@withTimeoutOrNull lastLocation.toCoordinates()
            }

            val location = getFirstAvailableLocation()
            if (location != null) {
                return@withTimeoutOrNull location.toCoordinates()
            }

            lastLocation?.toCoordinates()
        }
    }

    override suspend fun getCurrentLocationOrNull(): Coordinates? {
        if (!hasLocationPermission()) return null

        return withTimeoutOrNull(timeMillis = timeoutMilliseconds) {
            getFirstAvailableLocation()?.toCoordinates()
        }
    }

    private suspend fun getFirstAvailableLocation(): Location? = coroutineScope {
        val currentLocationDeferred = async { currentFixOrNull() }
        val activeLocationDeferred = async { requestSingleUpdateOrNull() }

        val currentLocation = currentLocationDeferred.await()
        if (currentLocation != null) {
            activeLocationDeferred.cancel()
            return@coroutineScope currentLocation
        }

        activeLocationDeferred.await()
    }

    private fun isLocationFresh(location: Location): Boolean {
        val locationAge = System.currentTimeMillis() - location.time
        return locationAge < maxLocationAgeMilliseconds
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        val coarseLocationGranted = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        return fineLocationGranted || coarseLocationGranted
    }

    @SuppressLint("MissingPermission")
    private suspend fun lastLocationOrNull(): Location? =
        fusedLocationProviderClient.lastLocation.awaitOrNull()

    @SuppressLint("MissingPermission")
    private suspend fun currentFixOrNull(): Location? =
        fusedLocationProviderClient.getCurrentLocation(locationPriority, null).awaitOrNull()

    @SuppressLint("MissingPermission")
    private suspend fun requestSingleUpdateOrNull(): Location? =
        suspendCancellableCoroutine { continuation: CancellableContinuation<Location?> ->
            val locationRequest = LocationRequest.Builder(locationPriority, 1000L)
                .setMinUpdateIntervalMillis(0)
                .setMaxUpdates(1)
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    fusedLocationProviderClient.removeLocationUpdates(this)
                    if (continuation.isActive) {
                        continuation.resume(value = location)
                    }
                }
            }

            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper(),
            )

            continuation.invokeOnCancellation {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            }
        }
}

private fun Location.toCoordinates(): Coordinates =
    Coordinates(
        latitude = latitude,
        longitude = longitude,
    )

private suspend fun <T> Task<T>.awaitOrNull(): T? =
    suspendCancellableCoroutine { continuation: CancellableContinuation<T?> ->
        val completionListener = OnCompleteListener<T> { task ->
            if (continuation.isActive) {
                continuation.resume(value = if (task.isSuccessful) task.result else null)
            }
        }

        addOnCompleteListener(completionListener)

        continuation.invokeOnCancellation {
            // Task completion listeners cannot be removed, but the result will be ignored
            // since continuation.isActive will be false
        }
    }