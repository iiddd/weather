package com.iiddd.weather.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.iiddd.weather.ui.navigation.MainTabsScreen

@Composable
fun MainView() {
    Navigator(MainTabsScreen()) {
        CurrentScreen()
    }
}