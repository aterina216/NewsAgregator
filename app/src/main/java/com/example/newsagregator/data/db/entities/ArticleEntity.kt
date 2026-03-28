package com.example.newsagregator.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey
    val url: String,
    val title: String,
    val description: String,
    val content: String,
    val imageUrl: String,
    val publishedAt: Long,
    val sourceName: String,
    val category: String,
    val isFavorite: Boolean = false
)