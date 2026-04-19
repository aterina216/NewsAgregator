package com.example.newsagregator.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

enum class Activities(val route: String) {

    HOME("home"),
    FAVORITES("favorites"),
    HISTORY("history"),
    SETTINGS("settings")
}