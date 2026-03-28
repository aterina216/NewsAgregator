package com.example.newsagregator.domain.models

data class Article(
    val url: String,
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val publishedAt: Long,
    val sourceName: String,
    val category: String,
    val isFavorite: Boolean
)