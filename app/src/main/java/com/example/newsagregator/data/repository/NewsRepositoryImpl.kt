package com.example.newsagregator.data.repository

import android.util.Log
import androidx.annotation.OptIn
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.newsagregator.data.db.database.ArticleDataBase
import com.example.newsagregator.data.db.mapper.Mapper.toDomain
import com.example.newsagregator.data.mediators.NewsRemoteMediator
import com.example.newsagregator.data.mediators.SearchRemoteMediator
import com.example.newsagregator.data.remote.api.NewsApi
import com.example.newsagregator.domain.models.Article
import com.example.newsagregator.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi,
    private val database: ArticleDataBase
): NewsRepository {


    @kotlin.OptIn(ExperimentalPagingApi::class)
    override fun getNews(category: String): Flow<PagingData<Article>> {
        Log.d("Repository", "getNews for $category")
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

    @kotlin.OptIn(ExperimentalPagingApi::class)
    override fun searchNews(query: String): Flow<PagingData<Article>> {
        val pagingSourceFactory = {database.getDao().getArticlesBySearchQuery(query)}
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = SearchRemoteMediator(api, database, query)
        ) {
            pagingSourceFactory()
        }.flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }
    }

    override suspend fun getArticleByUrl(url: String): Article? {
        val entity = database.getDao().selectArticleByUrl(url)
        if (entity != null) {
            return entity.toDomain()
        }
        else return null
    }

    override suspend fun addToFavorites(article: Article) {
        database.getDao().setFavorite(article.url, true)
    }

    override suspend fun removeFromFavorites(article: Article) {
        database.getDao().setFavorite(article.url, false)
    }

    override fun getFavoriteArticles(): Flow<PagingData<Article>> {
        val pagingSourceFactory = {
            database.getDao().getAllFavoritesArticles()
        }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false)
        ) {
            pagingSourceFactory()
        }.flow.map {
            pagingData ->
            pagingData.map { it.toDomain() }
        }

    }

    override fun getArticleFavoriteState(url: String): Flow<Boolean> {
        return database.getDao().getFavoriteStatus(url).map {
            it ?: false
        }
    }
}