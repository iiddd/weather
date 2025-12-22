package com.iiddd.weather.forecast.presentation.view

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.iiddd.weather.core.ui.components.LoadingSpinner
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastViewModel
import com.iiddd.weather.location.data.FusedLocationTracker
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.iiddd.weather.forecast.R as ForecastR

@Composable
fun DetailedWeatherScreen(
    viewModel: ForecastViewModel = koinViewModel(),
    initialLatitude: Double? = null,
    initialLongitude: Double? = null,
    initialCity: String? = null
) {
    val context = LocalContext.current

    val weatherState = viewModel.weather.collectAsState()
    val isLoadingState = viewModel.isLoading.collectAsState()

    if (initialLatitude != null && initialLongitude != null) {
        LaunchedEffect(
            key1 = initialLatitude,
            key2 = initialLongitude,
            key3 = initialCity
        ) {
            if (initialCity != null) {
                viewModel.loadWeather(
                    latitude = initialLatitude,
                    longitude = initialLongitude,
                    city = initialCity
                )
            } else {
                viewModel.loadWeatherWithGeocoding(
                    context = context,
                    latitude = initialLatitude,
                    longitude = initialLongitude
                )
            }
        }

        if (isLoadingState.value) {
            LoadingSpinner()
            return
        }

        DetailedWeatherContent(
            weatherState = weatherState,
            onRefresh = {
                if (initialCity != null) {
                    viewModel.loadWeather(
                        latitude = initialLatitude,
                        longitude = initialLongitude,
                        city = initialCity
                    )
                } else {
                    viewModel.loadWeatherWithGeocoding(
                        context = context,
                        latitude = initialLatitude,
                        longitude = initialLongitude
                    )
                }
            }
        )
        return
    }

    val activity = context as? Activity
    val fusedLocationTracker = remember { FusedLocationTracker(context = context) }
    val coroutineScope = rememberCoroutineScope()

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

    var shouldShowPermissionRationaleDialog by remember { mutableStateOf(false) }
    var shouldShowOpenSettingsDialog by remember { mutableStateOf(false) }
    var isInitialPermissionRequestCompleted by remember { mutableStateOf(false) }

    var hasRequestedWeatherForCurrentLocation by remember { mutableStateOf(false) }
    var shouldShowLoadingSpinnerForLocationFlow by remember { mutableStateOf(true) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        hasLocationPermission =
            permissionResults[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                    permissionResults[Manifest.permission.ACCESS_COARSE_LOCATION] == true

        isInitialPermissionRequestCompleted = true
    }

    fun isLocationPermissionPermanentlyDenied(): Boolean {
        if (activity == null) return false
        if (!isInitialPermissionRequestCompleted) return false

        val isFineLocationPermissionDenied =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED

        val isCoarseLocationPermissionDenied =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED

        val shouldShowFineLocationRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )

        val shouldShowCoarseLocationRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

        return (isFineLocationPermissionDenied && !shouldShowFineLocationRationale) ||
                (isCoarseLocationPermissionDenied && !shouldShowCoarseLocationRationale)
    }

    fun requestLocationPermission() {
        if (isLocationPermissionPermanentlyDenied()) {
            shouldShowOpenSettingsDialog = true
            return
        }

        val shouldShowRationale =
            activity?.let { currentActivity ->
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
            isInitialPermissionRequestCompleted = true
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    LaunchedEffect(key1 = Unit) {
        if (!hasLocationPermission && !isInitialPermissionRequestCompleted) {
            shouldShowLoadingSpinnerForLocationFlow = true
            requestLocationPermission()
        }
    }

    LaunchedEffect(key1 = hasLocationPermission) {
        if (!hasLocationPermission) {
            shouldShowLoadingSpinnerForLocationFlow = true
            hasRequestedWeatherForCurrentLocation = false
            return@LaunchedEffect
        }

        if (hasRequestedWeatherForCurrentLocation) return@LaunchedEffect

        shouldShowLoadingSpinnerForLocationFlow = true
        val coordinate = fusedLocationTracker.getLastKnownLocation()
        if (coordinate == null) {
            shouldShowLoadingSpinnerForLocationFlow = true
            return@LaunchedEffect
        }

        hasRequestedWeatherForCurrentLocation = true
        viewModel.loadWeatherWithGeocoding(
            context = context,
            latitude = coordinate.latitude,
            longitude = coordinate.longitude
        )
        shouldShowLoadingSpinnerForLocationFlow = false
    }

    val shouldShowLoadingSpinner =
        isLoadingState.value || (weatherState.value == null && shouldShowLoadingSpinnerForLocationFlow)

    if (shouldShowLoadingSpinner) {
        LoadingSpinner()
    } else {
        DetailedWeatherContent(
            weatherState = weatherState,
            onRefresh = {
                coroutineScope.launch {
                    if (!hasLocationPermission) {
                        shouldShowLoadingSpinnerForLocationFlow = true
                        requestLocationPermission()
                        return@launch
                    }

                    shouldShowLoadingSpinnerForLocationFlow = true
                    hasRequestedWeatherForCurrentLocation = false

                    val coordinate = fusedLocationTracker.getLastKnownLocation()
                    if (coordinate == null) {
                        shouldShowLoadingSpinnerForLocationFlow = true
                        return@launch
                    }

                    viewModel.loadWeatherWithGeocoding(
                        context = context,
                        latitude = coordinate.latitude,
                        longitude = coordinate.longitude
                    )
                    shouldShowLoadingSpinnerForLocationFlow = false
                }
            }
        )
    }

    if (shouldShowPermissionRationaleDialog) {
        AlertDialog(
            onDismissRequest = { shouldShowPermissionRationaleDialog = false },
            title = { Text(text = stringResource(id = ForecastR.string.permission_required)) },
            text = { Text(text = stringResource(id = ForecastR.string.permission_rationale)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        shouldShowPermissionRationaleDialog = false
                        isInitialPermissionRequestCompleted = true
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                ) { Text(text = stringResource(id = ForecastR.string.allow)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { shouldShowPermissionRationaleDialog = false }
                ) { Text(text = stringResource(id = ForecastR.string.cancel)) }
            }
        )
    }

    if (shouldShowOpenSettingsDialog) {
        AlertDialog(
            onDismissRequest = { shouldShowOpenSettingsDialog = false },
            title = { Text(text = stringResource(id = ForecastR.string.permission_blocked)) },
            text = { Text(text = stringResource(id = ForecastR.string.permission_blocked_text)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        shouldShowOpenSettingsDialog = false
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    }
                ) { Text(text = stringResource(id = ForecastR.string.open_settings)) }
            },
            dismissButton = {
                TextButton(
                    onClick = { shouldShowOpenSettingsDialog = false }
                ) { Text(text = stringResource(id = ForecastR.string.cancel)) }
            }
        )
    }
}