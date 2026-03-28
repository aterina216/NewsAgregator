package com.example.newsagregator.data.repository

import androidx.annotation.OptIn
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.newsagregator.data.db.database.ArticleDataBase
import com.example.newsagregator.data.db.mapper.Mapper.toDomain
import com.example.newsagregator.data.remote.NewsRemoteMediator
import com.example.newsagregator.data.remote.api.NewsApi
import com.example.newsagregator.domain.models.Article
import com.example.newsagregator.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsRepositoryImpl(
    private val api: NewsApi,
    private val database: ArticleDataBase
): NewsRepository {


    @kotlin.OptIn(ExperimentalPagingApi::class)
    override fun getNews(category: String): Flow<PagingData<Article>> {
        val pagingSourceFactory = {database.getDao().getArticlesByCategory(category)}
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = NewsRemoteMediator(api, database, category)
        ) {
            pagingSourceFactory()
        }.flow.map {
            pagingData -> pagingData.map { it.toDomain() }
        }
    }
}