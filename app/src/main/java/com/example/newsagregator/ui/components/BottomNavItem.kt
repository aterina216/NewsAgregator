package com.example.newsagregator.ui.components

import com.example.myapplication.R

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
    val selectedIcon: Int? = null
) {

    object Home: BottomNavItem("home", "Главная",
        R.drawable.outline_home_24, R.drawable.round_home_24
    )
}