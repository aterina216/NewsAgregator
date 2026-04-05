package com.example.newsagregator.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.tooling.ComposeToolingApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.newsagregator.ui.viewmodels.NewsFeedViewModel
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.newsagregator.ui.components.BottomNavigationBar
import com.example.newsagregator.ui.screens.ArticleDetailScreen
import com.example.newsagregator.ui.screens.Home

@Composable
fun InitNavigation(viewmodel: NewsFeedViewModel) {

    val navController = rememberNavController()

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
        }
    }
}