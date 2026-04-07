package com.example.newsagregator.domain.repository

import androidx.paging.PagingData
import com.example.newsagregator.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(category: String): Flow<PagingData<Article>>
    fun searchNews(query: String): Flow<PagingData<Article>>

    suspend fun getArticleByUrl(url: String): Article?

    suspend fun addToFavorites(article: Article)
    suspend fun removeFromFavorites(article: Article)
    fun getFavoriteArticles(): Flow<PagingData<Article>>

    fun getArticleFavoriteState(url: String): Flow<Boolean>
}