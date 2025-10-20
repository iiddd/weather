package com.iiddd.weather.location.data

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.iiddd.weather.location.domain.Coordinate
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

    override suspend fun getLastKnownLocation(): Coordinate? {
        if (!hasLocationPermission()) return null

        lastLocationOrNull()?.let { return it.toCoordinate() }

        return withTimeoutOrNull(timeoutMs) {
            currentFixOrNull()?.toCoordinate()
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
}

/* ===== Helpers ===== */

private fun Location.toCoordinate() = Coordinate(latitude, longitude)

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