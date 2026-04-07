package com.example.newsagregator.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.myapplication.R
import com.example.newsagregator.ui.viewmodels.NewsFeedViewModel
import com.example.newsagregator.utils.Format.formatDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    viewModel: NewsFeedViewModel,
    url: String,
    onBackPressed: () -> Unit
) {
    val article by viewModel.currentArticle.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getArticleByUrl(url)
        viewModel.loadFavoriteStatus(url)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Новость")
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (article != null) {
                                viewModel.toggleFavorite(article!!, isFavorite)
                            }
                        }
                    ) {
                        Icon(
                            painter = if (isFavorite) painterResource(R.drawable.baseline_favorite_24)
                            else painterResource(R.drawable.outline_favorite_24),
                            contentDescription = if (isFavorite) "Удалить из избранного"
                            else "Добавить в избранное",
                            tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            )
        }
    ) {
        paddingValues ->
        if(isLoading) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else if (article == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Новость не найдена")
            }
        }
        else  {
            LazyColumn(modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
                contentPadding = PaddingValues(16.dp)) {
                item {
                    if(!article!!.imageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(article!!.imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Text(
                        text = article!!.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = article!!.sourceName,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = formatDate(article!!.publishedAt),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if(!article!!.description.isNullOrEmpty()) {
                        Text(
                            text = article!!.description,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if(!article!!.content.isNullOrEmpty()) {
                        Text(
                            text = article!!.content,
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 24.sp
                        )
                    }
                }
            }
        }
    }
}