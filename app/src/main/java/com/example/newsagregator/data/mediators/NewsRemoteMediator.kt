package com.example.newsagregator.data.mediators

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.newsagregator.data.db.database.ArticleDataBase
import com.example.newsagregator.data.db.entities.ArticleEntity
import com.example.newsagregator.data.remote.api.NewsApi
import com.example.newsagregator.data.remote.mapper.Mapper.toEntity
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val api: NewsApi,
    private val database: ArticleDataBase,
    private val category: String
): RemoteMediator<Int, ArticleEntity>() {

    private var nextPage = 1

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleEntity>
    ): MediatorResult {

        Log.d("RemoteMediator", "load called: type=$loadType, category=$category")

        val page = when (loadType) {
            LoadType.REFRESH -> {
                nextPage = 1
                1
            }
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> nextPage
        }

        return try {
            val response = api.getActualNews(
                country = "us",
                category = category,
                pageSize = state.config.pageSize,
                page = page
            )

            Log.d("RemoteMediator", "Response code: ${response.code()}")
            if (!response.isSuccessful) {
                return MediatorResult.Success(endOfPaginationReached = true)
            }

            val articles = response.body()?.articles ?: emptyList()
            val entities = articles.map { it.toEntity(category) }

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.getDao().clearCategory(category)
                }
                database.getDao().insertAll(entities)
            }

            if(articles.isNotEmpty()) {
                nextPage++
            }

            MediatorResult.Success(endOfPaginationReached = articles.isEmpty())
        }
        catch (e: IOException) {
            Log.e("RemoteMediator", "IOException: ${e.message}", e)
            MediatorResult.Success(endOfPaginationReached = true)
        }
        catch (e: HttpException) {
            Log.e("RemoteMediator", "HttpException: ${e.message}, code: ${e.code()}", e)
            MediatorResult.Success(endOfPaginationReached = true)
        }
    }
}