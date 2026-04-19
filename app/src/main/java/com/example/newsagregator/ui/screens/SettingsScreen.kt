package com.example.newsagregator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.newsagregator.data.preferences.ThemeMode
import com.example.newsagregator.data.preferences.ThemePreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.newsagregator.data.preferences.Activities
import com.example.newsagregator.ui.components.StartDestinationOption
import com.example.newsagregator.ui.components.ThemeOption
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {

    val context = LocalContext.current
    val themePref = remember { ThemePreferences(context) }
    var selectedTheme by remember { mutableStateOf(ThemeMode.SYSTEM) }
    var selectedStartDest by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        selectedTheme = themePref.themeFlow.first()
        selectedStartDest = themePref.startDestinationFlow.first()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад")
                    }
                }
            )
        }
    ) {
        paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                ThemeOption(
                    title = "Светлая",
                    isSelected = selectedTheme == ThemeMode.LIGHT,
                    onClick = {
                        selectedTheme = ThemeMode.LIGHT
                        CoroutineScope(Dispatchers.IO).launch {
                            themePref.setTheme(ThemeMode.LIGHT)
                        }
                    }
                )
            }
            item {
                ThemeOption(
                    title = "Темная",
                    isSelected = selectedTheme == ThemeMode.DARK,
                    onClick = {
                        selectedTheme = ThemeMode.DARK
                        CoroutineScope(Dispatchers.IO).launch {
                            themePref.setTheme(ThemeMode.DARK)
                        }
                    }
                )
            }
            item {
                ThemeOption(
                    title = "Как в системе",
                    isSelected = selectedTheme == ThemeMode.SYSTEM,
                    onClick = {
                        selectedTheme = ThemeMode.SYSTEM
                        CoroutineScope(Dispatchers.IO).launch {
                            themePref.setTheme(ThemeMode.SYSTEM)
                        }
                    }
                )
            }

            item {
                Text("Стартовый экран", style = MaterialTheme.typography.titleMedium)
            }
            item {
                StartDestinationOption(
                    title = "Главная",
                    route = Activities.HOME.route,
                    isSelected = selectedStartDest == Activities.HOME.route,
                    onClick = {
                        selectedStartDest = Activities.HOME.route
                        CoroutineScope(Dispatchers.IO).launch {
                            themePref.setStartDestination(Activities.HOME.route)
                        }
                    }
                )
            }
            item {
                StartDestinationOption(
                    title = "История",
                    route = Activities.HISTORY.route,
                    isSelected = selectedStartDest == Activities.HISTORY.route,
                    onClick = {
                        selectedStartDest = Activities.HISTORY.route
                        CoroutineScope(Dispatchers.IO).launch {
                            themePref.setStartDestination(Activities.HISTORY.route)
                        }
                    }
                )
            }
            item {
                StartDestinationOption(
                    title = "Избранное",
                    route = Activities.FAVORITES.route,
                    isSelected = selectedStartDest == Activities.FAVORITES.route,
                    onClick = {
                        selectedStartDest = Activities.FAVORITES.route
                        CoroutineScope(Dispatchers.IO).launch {
                            themePref.setStartDestination(Activities.FAVORITES.route)
                        }
                    }
                )
            }
            item {
                StartDestinationOption(
                    title = "Настройки",
                    route = Activities.SETTINGS.route,
                    isSelected = selectedStartDest == Activities.SETTINGS.route,
                    onClick = {
                        selectedStartDest = Activities.SETTINGS.route
                        CoroutineScope(Dispatchers.IO).launch {
                            themePref.setStartDestination(Activities.SETTINGS.route)
                        }
                    }
                )
            }
        }
    }
}