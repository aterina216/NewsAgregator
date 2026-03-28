package com.example.newsagregator.domain.repository

import androidx.paging.PagingData
import com.example.newsagregator.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getNews(category: String): Flow<PagingData<Article>>
}