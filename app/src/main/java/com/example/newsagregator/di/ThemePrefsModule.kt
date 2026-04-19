package com.example.newsagregator.di

import android.content.Context
import com.example.newsagregator.data.preferences.ThemePreferences
import dagger.Module
import dagger.Provides

@Module
class ThemePrefsModule {

    @Provides
    fun provideThemePrefs(context: Context): ThemePreferences {
        return ThemePreferences(context)
    }
}