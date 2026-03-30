package com.example.newsagregator.data.remote.mapper

import androidx.compose.ui.text.intl.Locale
import com.example.newsagregator.data.db.entities.ArticleEntity
import com.example.newsagregator.data.remote.dto.Article
import java.text.SimpleDateFormat

object Mapper {

    fun Article.toEntity(category: String): ArticleEntity = ArticleEntity(
        url = url,
        title = title,
        description = description ?: "",
        content = content ?: "",
        imageUrl = urlToImage ?: " ",
        publishedAt = publishedAt.toTimeStamp(),
        sourceName = source.name,
        category = category,
        isFavorite = false
    )

    fun String.toTimeStamp(): Long {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.US)
            format.parse(this)?.time ?: 0L
        }
        catch (e: Exception) {
            0L
        }
    }
}