package com.iiddd.weather.forecast.presentation.view.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.iiddd.weather.forecast.R as ForecastResource

@Composable
fun rememberLocationPermissionController(): LocationPermissionController {
    val context = LocalContext.current
    val activity = context as? Activity

    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var shouldShowPermissionRationaleDialog by rememberSaveable { mutableStateOf(false) }
    var shouldShowOpenSettingsDialog by rememberSaveable { mutableStateOf(false) }
    var hasCompletedFirstRequest by rememberSaveable { mutableStateOf(false) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults: Map<String, Boolean> ->
        hasLocationPermission =
            permissionResults[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissionResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        hasCompletedFirstRequest = true
    }

    fun isPermissionPermanentlyDenied(): Boolean {
        if (activity == null) return false
        if (!hasCompletedFirstRequest) return false

        val fineDenied =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED

        val coarseDenied =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED

        val showFineRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        val showCoarseRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        return (fineDenied && !showFineRationale) || (coarseDenied && !showCoarseRationale)
    }

    fun requestPermissionInternal() {
        if (isPermissionPermanentlyDenied()) {
            shouldShowOpenSettingsDialog = true
            return
        }

        val shouldShowRationale =
            activity?.let { currentActivity: Activity ->
                ActivityCompat.shouldShowRequestPermissionRationale(
                    currentActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                    currentActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } ?: false

        if (shouldShowRationale) {
            shouldShowPermissionRationaleDialog = true
        } else {
            hasCompletedFirstRequest = true
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (shouldShowPermissionRationaleDialog) {
        AlertDialog(
            onDismissRequest = { shouldShowPermissionRationaleDialog = false },
            title = { Text(text = stringResource(id = ForecastResource.string.permission_required)) },
            text = { Text(text = stringResource(id = ForecastResource.string.permission_rationale)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        shouldShowPermissionRationaleDialog = false
                        hasCompletedFirstRequest = true
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                ) { Text(text = stringResource(id = ForecastResource.string.allow)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { shouldShowPermissionRationaleDialog = false }
                ) { Text(text = stringResource(id = ForecastResource.string.cancel)) }
            }
        )
    }

    if (shouldShowOpenSettingsDialog) {
        AlertDialog(
            onDismissRequest = { shouldShowOpenSettingsDialog = false },
            title = { Text(text = stringResource(id = ForecastResource.string.permission_blocked)) },
            text = { Text(text = stringResource(id = ForecastResource.string.permission_blocked_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        shouldShowOpenSettingsDialog = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) { Text(text = stringResource(id = ForecastResource.string.open_settings)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { shouldShowOpenSettingsDialog = false }
                ) { Text(text = stringResource(id = ForecastResource.string.cancel)) }
            }
        )
    }

    return remember {
        LocationPermissionController(
            hasLocationPermissionProvider = { hasLocationPermission },
            requestLocationPermission = { requestPermissionInternal() }
        )
    }
}

class LocationPermissionController internal constructor(
    private val hasLocationPermissionProvider: () -> Boolean,
    private val requestLocationPermission: () -> Unit
) {
    val hasLocationPermission: Boolean
        get() = hasLocationPermissionProvider()

    fun requestLocationPermission() {
        requestLocationPermission.invoke()
    }
}