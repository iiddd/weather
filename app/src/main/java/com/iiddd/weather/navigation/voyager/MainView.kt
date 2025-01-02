package com.iiddd.weather.navigation.voyager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator

@Composable
private fun MainContent() {
    TabNavigator(HomeTab) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    listOf(HomeTab, SettingsTab).forEach { tab ->
                        TabNavigationItem(tab = tab)
                    }
                }
            },
            content = { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    CurrentTab()
                }
            }
        )
    }
}