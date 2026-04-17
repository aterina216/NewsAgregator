package com.example.newsagregator.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newsagregator.data.db.entities.ArticleEntity
import com.example.newsagregator.data.db.entities.UrlAndViewAt
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<ArticleEntity>)

    @Query("SELECT * FROM articles")
    suspend fun getAllArticles(): List<ArticleEntity>

    @Query("SELECT * FROM articles WHERE category = :category ORDER BY publishedAt DESC")
    fun getArticlesByCategory(category: String): PagingSource<Int, ArticleEntity>

    @Query("DELETE FROM articles WHERE category = :category")
    suspend fun clearCategory(category: String)

    @Update
    suspend fun update(article: ArticleEntity)

    @Query("DELETE FROM articles WHERE searchQuery = :query")
    suspend fun clearSearchResults(query: String)

    @Query("SELECT * FROM articles WHERE searchQuery = :query ORDER BY publishedAt DESC")
    fun getArticlesBySearchQuery(query: String): PagingSource<Int, ArticleEntity>

    @Query("SELECT * FROM articles WHERE url =:url")
    suspend fun selectArticleByUrl(url: String): ArticleEntity?

    @Query("SELECT * FROM articles WHERE isFavorite = 1 ORDER by publishedAt DESC")
    fun getAllFavoritesArticles(): PagingSource<Int, ArticleEntity>

    @Query("UPDATE articles SET isFavorite = :isFavorite WHERE url = :url")
    suspend fun setFavorite(url: String, isFavorite: Boolean)

    @Query("SELECT isFavorite FROM articles WHERE url = :url")
    fun getFavoriteStatus(url: String): Flow<Boolean?>

    @Query("SELECT url FROM articles WHERE isFavorite = 1")
    suspend fun getFavoritesUrls(): List<String>

    @Query("UPDATE articles SET viewAt =:viewAt WHERE url =:url")
    suspend fun updateViewAt(viewAt: Long, url: String)

    @Query("SELECT url, viewAt FROM articles WHERE category = :category")
    suspend fun getViewAtByCategory(category: String): List<UrlAndViewAt>

    @Query("SELECT * FROM articles WHERE viewAt > 0 ORDER BY viewAt DESC")
    fun getHistory(): PagingSource<Int, ArticleEntity>

    @Query("DELETE FROM articles WHERE viewAt > 0")
    suspend fun clearHistory()
}