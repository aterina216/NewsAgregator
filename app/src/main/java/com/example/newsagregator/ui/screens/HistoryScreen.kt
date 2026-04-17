package com.example.newsagregator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.newsagregator.ui.components.NewsCard
import com.example.newsagregator.ui.viewmodels.NewsFeedViewModel
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: NewsFeedViewModel,
    navController: NavController
) {
    val historyPagingItems = viewModel.historyArticles.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("История просмотров") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {navController.navigateUp()}
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {viewModel.clearHistory()}) {
                        Icon(Icons.Default.Delete, contentDescription = "Очистить историю")
                    }
                }
            )
        }
    ) {
        paddingValues ->
        when {
            historyPagingItems.loadState.refresh is LoadState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            historyPagingItems.loadState.refresh is LoadState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Не удалось загрузить историю",
                        color = MaterialTheme.colorScheme.error)
                }
            }
            historyPagingItems.itemCount == 0 -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Вы еще не читали новости",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(historyPagingItems.itemCount) {
                        index ->
                        val article = historyPagingItems[index]
                        if(article != null) {
                            NewsCard(
                                article,
                                onClick = {
                                    val encodeUrl = URLEncoder.encode(article.url, "utf-8")
                                    navController.navigate("article/$encodeUrl")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}