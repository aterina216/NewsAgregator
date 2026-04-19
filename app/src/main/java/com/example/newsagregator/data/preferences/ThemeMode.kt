package com.example.newsagregator.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")
enum class ThemeMode(val value: Int) {
    LIGHT(0),
    DARK(1),
    SYSTEM(2)
}