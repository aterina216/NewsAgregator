package com.example.newsagregator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.newsagregator.NewsApp
import com.example.newsagregator.data.preferences.ThemeMode
import com.example.newsagregator.data.preferences.ThemePreferences
import com.example.newsagregator.ui.navigation.InitNavigation
import com.example.newsagregator.ui.theme.NewsAgregatorTheme
import com.example.newsagregator.ui.viewmodels.NewViewmodelFactory
import com.example.newsagregator.ui.viewmodels.NewsFeedViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewmodelFactory: NewViewmodelFactory

    @Inject lateinit var prefs: ThemePreferences

    private val viewModel: NewsFeedViewModel by viewModels { viewmodelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as NewsApp).appComponent.inject(this)
        enableEdgeToEdge()
        setContent {
            val themeMode by prefs.themeFlow.collectAsState(ThemeMode.SYSTEM)
            val darkTheme = when (themeMode){
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            MaterialTheme (
                colorScheme = if (darkTheme) darkColorScheme() else lightColorScheme()
            ) {
                InitNavigation(viewModel)
            }
        }
    }
}