package com.iiddd.weather.ui.navigation

fun NavigationBackStack.popSafe(): Boolean {
    if (entries.size <= 1) return false
    entries.removeAt(entries.lastIndex)
    return true
}