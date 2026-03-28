package com.example.newsagregator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.tooling.ComposeToolingApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.newsagregator.ui.viewmodels.NewsFeedViewModel
import androidx.navigation.compose.composable
import com.example.newsagregator.ui.screens.Home

@Composable
fun InitNavigation(viewmodel: NewsFeedViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            Home(viewmodel)
        }
    }
}