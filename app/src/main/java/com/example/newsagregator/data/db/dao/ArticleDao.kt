package com.example.newsagregator.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.newsagregator.data.db.entities.ArticleEntity

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
}