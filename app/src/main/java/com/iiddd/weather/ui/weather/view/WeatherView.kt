package com.iiddd.weather.ui.weather.view

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.iiddd.weather.navigation.Screen

@Composable
fun WeatherView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Header()
            CurrentWeather()
            Forecast()
        }
    }
}

@Composable
fun Header() {
    Row {
        LocationWidget()
        ThemeSwitch()
    }
}

@Composable
fun LocationWidget() {

}

@Composable
fun ThemeSwitch() {

}

@Composable
fun CurrentWeather() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Clear")
        Text(text = "13")
    }
}

@Composable
fun Forecast() {

}

@Composable
@Preview
fun WeatherPreview() {
    WeatherView()
}