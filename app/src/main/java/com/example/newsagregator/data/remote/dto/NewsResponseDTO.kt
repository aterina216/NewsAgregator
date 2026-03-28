package com.example.newsagregator.data.remote.dto

data class NewsResponseDTO(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)