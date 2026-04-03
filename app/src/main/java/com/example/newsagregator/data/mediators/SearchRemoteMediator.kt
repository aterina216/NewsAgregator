package com.example.newsagregator.data.mediators

import android.util.Log
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
class SearchRemoteMediator(
    private val api: NewsApi,
    private val database: ArticleDataBase,
    private val query: String
): RemoteMediator<Int, ArticleEntity>() {

    private var nextPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {
        val page = when(loadType) {
            LoadType.REFRESH -> {
                nextPage = 1
                1
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> nextPage
        }

        return try {
            val response = api.searchEverything(
                query = query,
                pageSize = state.config.pageSize,
                page = page
            )

            if(!response.isSuccessful) {
                return MediatorResult.Error(retrofit2.HttpException(response))
            }

            val articles = response.body()?.articles ?: emptyList()
            val entities = articles.map { it.toEntity("search", searchQuery = query) }

            database.withTransaction {
                if(loadType == LoadType.REFRESH) {
                    database.getDao().clearSearchResults(query)
                }
                database.getDao().insertAll(entities)
            }
            if(articles.isNotEmpty()) nextPage++
            MediatorResult.Success(articles.isEmpty())
        }
        catch (e: IOException) {
            MediatorResult.Error(e)
        }
        catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}