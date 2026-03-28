package com.example.newsagregator.data.db.mapper

import com.example.newsagregator.data.db.entities.ArticleEntity
import com.example.newsagregator.domain.models.Article

object Mapper {

    fun ArticleEntity.toDomain(): Article = Article(
        url = url,
        title = title,
        description = description,
        content = content,
        imageUrl = imageUrl,
        publishedAt = publishedAt,
        sourceName = sourceName,
        category = category,
        isFavorite = isFavorite
    )
}