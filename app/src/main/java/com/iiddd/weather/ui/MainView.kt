package com.iiddd.weather.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.iiddd.weather.ui.components.TabNavigationItem
import com.iiddd.weather.ui.navigation.HomeTab
import com.iiddd.weather.ui.navigation.SearchTab
import com.iiddd.weather.ui.navigation.SettingsTab

private val homeTab = HomeTab()
private val searchTab = SearchTab()
private val settingsTab = SettingsTab()

@Composable
fun MainView() {
    TabNavigator(homeTab) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    listOf(homeTab, searchTab, settingsTab).forEach { tab ->
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