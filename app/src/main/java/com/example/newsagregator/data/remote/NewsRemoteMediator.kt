package com.example.newsagregator.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import coil.network.HttpException
import com.example.newsagregator.data.db.database.ArticleDataBase
import com.example.newsagregator.data.db.entities.ArticleEntity
import com.example.newsagregator.data.remote.api.NewsApi
import com.example.newsagregator.data.remote.mapper.Mapper.toEntity
import okio.IOException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val api: NewsApi,
    private val database: ArticleDataBase,
    private val category: String
): RemoteMediator<Int, ArticleEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastItem = state.lastItemOrNull()
                if(lastItem == null) 1
                else state.pages.lastOrNull()?.let {
                    pageData ->
                    (pageData.data.size / state.config.pageSize) + 1
                } ?: 1
            }
        }

        return try {
            val response = api.getActualNews(
                country = "us",
                category = category,
                pageSize = state.config.pageSize,
                page = page
            )

            val articles = response.body()?.articles ?: emptyList()
            val entities = articles.map { it.toEntity(category) }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.getDao().clearCategory(category)
                }
                database.getDao().insertAll(entities)
            }
            MediatorResult.Success(endOfPaginationReached = articles.isEmpty())
        }
        catch (e: IOException) {
            MediatorResult.Error(e)
        }
        catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}