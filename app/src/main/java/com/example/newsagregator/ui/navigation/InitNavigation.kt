package com.example.newsagregator.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.tooling.ComposeToolingApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.newsagregator.ui.viewmodels.NewsFeedViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsagregator.data.preferences.ThemePreferences
import com.example.newsagregator.ui.components.BottomNavigationBar
import com.example.newsagregator.ui.screens.ArticleDetailScreen
import com.example.newsagregator.ui.screens.FavoritesScreen
import com.example.newsagregator.ui.screens.HistoryScreen
import com.example.newsagregator.ui.screens.Home
import com.example.newsagregator.ui.screens.SettingsScreen
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@SuppressLint("RememberReturnType")
@Composable
fun InitNavigation(viewmodel: NewsFeedViewModel) {

    val navController = rememberNavController()

    val context = LocalContext.current
    val themePref = remember { ThemePreferences(context) }
    LaunchedEffect(Unit) {
        val savedRoute = themePref.startDestinationFlow.first()
        if (savedRoute != "home") {
            navController.navigate(savedRoute) {
                popUpTo(0) { inclusive = true }  // очищаем весь стек
                launchSingleTop = true
            }
        }
    }


    Scaffold(bottomBar = {
        BottomNavigationBar(navController)
    }) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                Home(viewmodel, navController)
            }
            composable(
                route = "article/{articleUrl}",
                arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
            ) { backStackEntry ->
                val url = backStackEntry.arguments?.getString("articleUrl") ?: ""  // ✅ было "url" → стало "articleUrl"
                ArticleDetailScreen(
                    viewmodel,
                    url = url,                     // передайте url в экран
                    onBackPressed = { navController.popBackStack() }
                )
            }
            composable("favorites") {
                FavoritesScreen(viewmodel, navController)
            }
            composable("history") {
                HistoryScreen(viewmodel, navController)
            }
            composable("settings") {
                SettingsScreen(navController)
            }
        }
    }
}