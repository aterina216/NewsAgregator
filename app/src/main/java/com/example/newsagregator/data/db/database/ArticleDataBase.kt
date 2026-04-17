package com.example.newsagregator.data.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.newsagregator.data.db.dao.ArticleDao
import com.example.newsagregator.data.db.entities.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 3, exportSchema =  false)
abstract class ArticleDataBase: RoomDatabase() {

    abstract fun getDao(): ArticleDao

}