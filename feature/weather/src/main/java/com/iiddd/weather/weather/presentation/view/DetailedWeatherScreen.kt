package com.iiddd.weather.weather.presentation.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.iiddd.weather.location.data.FusedLocationTracker
import com.iiddd.weather.weather.presentation.view.localcomponents.WeatherView
import com.iiddd.weather.weather.presentation.viewmodel.DetailedScreenViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailedWeatherScreen(viewModel: DetailedScreenViewModel = koinViewModel()) {
    val weatherState = viewModel.weather.collectAsState()
    val context = LocalContext.current
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

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        hasPermission = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            val coord = tracker.getLastKnownLocation()
            if (coord != null) {
                viewModel.loadWeather(coord.latitude, coord.longitude)
            } else {
                viewModel.loadWeather(defaultLat, defaultLon)
            }
        } else {
            viewModel.loadWeather(defaultLat, defaultLon)
        }
    }

    WeatherView(
        weatherState = weatherState,
        onRefresh = {
            scope.launch {
                if (!hasPermission) {
                    permissionLauncher.launch(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                    )
                } else {
                    val coord = tracker.getLastKnownLocation()
                    viewModel.loadWeather(coord?.latitude ?: defaultLat, coord?.longitude ?: defaultLon)
                }
            }
        },
        onRequestPermission = {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            )
        }
    )
}