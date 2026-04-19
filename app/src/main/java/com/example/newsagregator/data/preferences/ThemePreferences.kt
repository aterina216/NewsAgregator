package com.example.newsagregator.data.preferences

import android.content.Context
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemePreferences(context: Context) {

    private val dataStore = context.dataStore
    private val themeKey = intPreferencesKey("theme_mode")

    private val startDestKey = stringPreferencesKey("start_destination")

    val themeFlow: Flow<ThemeMode> = dataStore.data.map {
        prefs ->
        val value = prefs[themeKey] ?: ThemeMode.SYSTEM.value
        ThemeMode.values().find {
            it.value == value
        } ?: ThemeMode.SYSTEM
    }

    val startDestinationFlow: Flow<String> = dataStore.data.map {
        prefs ->
        prefs[startDestKey] ?: "home"
    }

    suspend fun setTheme(mode: ThemeMode) {
        dataStore.edit {
            prefs ->
            prefs[themeKey] = mode.value
        }
    }

    suspend fun setStartDestination(route: String) {
        dataStore.edit {
            prefs ->
            prefs[startDestKey] = route
        }
    }
}