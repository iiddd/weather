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
import com.iiddd.weather.forecast.presentation.viewmodel.ForecastViewModel
import com.iiddd.weather.location.data.FusedLocationTracker
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.iiddd.weather.forecast.R as ForecastR

@Composable
fun DetailedWeatherScreen(
    viewModel: ForecastViewModel = koinViewModel(),
    initialLat: Double? = null,
    initialLon: Double? = null,
    initialCity: String? = null
) {
    if (initialLat != null && initialLon != null) {
        val weatherState = viewModel.weather.collectAsState()
        LaunchedEffect(initialLat, initialLon, initialCity) {
            viewModel.loadWeather(initialLat, initialLon, initialCity)
        }
        DetailedWeatherContent(
            weatherState = weatherState,
            onRefresh = {
                viewModel.loadWeather(initialLat, initialLon, initialCity)
            }
        )
        return
    }

    val weatherState = viewModel.weather.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity
    val tracker = remember { FusedLocationTracker(context) }
    val scope = rememberCoroutineScope()

    val defaultLat = 52.35
    val defaultLon = 4.91

    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var showRationale by remember { mutableStateOf(false) }
    var showOpenSettings by remember { mutableStateOf(false) }
    var initialPermissionRequested by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    fun isPermissionPermanentlyDenied(): Boolean {
        if (activity == null) return false
        if (!initialPermissionRequested) return false
        val fineDenied = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        val coarseDenied = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
        val shouldShowFine = ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val shouldShowCoarse = ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return (fineDenied && !shouldShowFine) || (coarseDenied && !shouldShowCoarse)
    }

    fun requestLocationPermission() {
        if (isPermissionPermanentlyDenied()) {
            showOpenSettings = true
        } else {
            val shouldShow = activity?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(
                            it,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
            } ?: false

            if (shouldShow) {
                showRationale = true
            } else {
                initialPermissionRequested = true
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!hasPermission && !initialPermissionRequested) {
            requestLocationPermission()
        }
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            val coord = tracker.getLastKnownLocation()
            val lat = coord?.latitude ?: defaultLat
            val lon = coord?.longitude ?: defaultLon
            viewModel.loadWeatherWithGeocoding(context, lat, lon)
        } else {
            viewModel.loadWeather(defaultLat, defaultLon)
        }
    }

    DetailedWeatherContent(
        weatherState = weatherState,
        onRefresh = {
            scope.launch {
                if (!hasPermission) {
                    requestLocationPermission()
                } else {
                    val coordinate = tracker.getLastKnownLocation()
                    val latitude = coordinate?.latitude ?: defaultLat
                    val longitude = coordinate?.longitude ?: defaultLon
                    viewModel.loadWeatherWithGeocoding(context, latitude, longitude)
                }
            }
        }
    )

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text(text = stringResource(ForecastR.string.permission_required)) },
            text = { Text(text = stringResource(ForecastR.string.permission_rationale)) },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    initialPermissionRequested = true
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }) { Text(stringResource(ForecastR.string.allow)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showRationale = false
                }) { Text(stringResource(ForecastR.string.cancel)) }
            }
        )
    }

    if (showOpenSettings) {
        AlertDialog(
            onDismissRequest = { showOpenSettings = false },
            title = { Text(text = stringResource(ForecastR.string.permission_blocked)) },
            text = { Text(text = stringResource(ForecastR.string.permission_blocked_text)) },
            confirmButton = {
                TextButton(onClick = {
                    showOpenSettings = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) { Text(stringResource(ForecastR.string.open_settings)) }
            },
            dismissButton = {
                TextButton(onClick = {
                    showOpenSettings = false
                }) { Text(stringResource(ForecastR.string.cancel)) }
            }
        )
    }
}