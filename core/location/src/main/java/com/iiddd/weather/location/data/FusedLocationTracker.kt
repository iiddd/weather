package com.iiddd.weather.location.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
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
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

class FusedLocationTracker(
    private val context: Context,
    private val fused: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context),
    private val timeoutMs: Long = 5_000L,
    private val priority: Int = Priority.PRIORITY_BALANCED_POWER_ACCURACY
) : LocationTracker {

    private val tag = "FusedLocationTracker"

    override suspend fun getLastKnownLocation(): Coordinates? {
        if (!hasLocationPermission()) return null

        // Ensure the whole operation cannot exceed the timeout.
        return withTimeoutOrNull(timeMillis = timeoutMs) {
            // Try current location first
            val current = currentFixOrNull()
            if (current != null) {
                Log.d(
                    tag,
                    "currentFix -> lat=${current.latitude}, lon=${current.longitude}, provider=${current.provider}"
                )
                return@withTimeoutOrNull current.toCoordinate()
            }

            // Single update request fallback
            val active = requestSingleUpdateOrNull()
            if (active != null) {
                Log.d(
                    tag,
                    "activeFix -> lat=${active.latitude}, lon=${active.longitude}, provider=${active.provider}"
                )
                return@withTimeoutOrNull active.toCoordinate()
            }

            // Cached location fallback
            val last = lastLocationOrNull()
            if (last != null) {
                Log.d(
                    tag,
                    "lastLocation (fallback) -> lat=${last.latitude}, lon=${last.longitude}, provider=${last.provider}"
                )
                return@withTimeoutOrNull last.toCoordinate()
            }

            null
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    private suspend fun lastLocationOrNull(): Location? =
        fused.lastLocation.awaitOrNull()

    @SuppressLint("MissingPermission")
    private suspend fun currentFixOrNull(): Location? =
        fused.getCurrentLocation(priority, null).awaitOrNull()

    @SuppressLint("MissingPermission")
    private suspend fun requestSingleUpdateOrNull(): Location? =
        suspendCancellableCoroutine { continuation: CancellableContinuation<Location?> ->
            val request = LocationRequest.Builder(priority, 1000L)
                .setMinUpdateIntervalMillis(0)
                .build()

            val callback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    val location = result.lastLocation
                    if (!continuation.isActive) return
                    continuation.resume(location)
                }
            }

            fused.requestLocationUpdates(request, callback, Looper.getMainLooper())
            continuation.invokeOnCancellation {
                try {
                    fused.removeLocationUpdates(callback)
                } catch (_: Exception) { /* ignore */
                }
            }
        }
}

/* ===== Helpers ===== */

private fun Location.toCoordinate() = Coordinates(latitude, longitude)

private suspend fun <T> Task<T>.awaitOrNull(onCancel: (() -> Unit)? = null): T? =
    suspendCancellableCoroutine { continuation: CancellableContinuation<T?> ->
        val listener = OnCompleteListener<T> { task ->
            if (!continuation.isActive) return@OnCompleteListener
            continuation.resume(if (task.isSuccessful) task.result else null)
        }

        addOnCompleteListener(listener)

        continuation.invokeOnCancellation {
            onCancel?.invoke()
            runCatching {
                val method = this::class.java.methods
                    .firstOrNull { it.name == "removeOnCompleteListener" }
                method?.invoke(this, listener)
            }
        }
    }