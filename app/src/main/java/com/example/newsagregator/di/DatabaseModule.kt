package com.example.newsagregator.di

import android.content.Context
import androidx.core.content.pm.PermissionInfoCompat
import androidx.room.Room
import com.example.newsagregator.data.db.database.ArticleDataBase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDataBase(context: Context): ArticleDataBase {
        return Room.databaseBuilder(
            context = context,
            klass = ArticleDataBase::class.java,
            name = "articles_db"
        ).build()
    }
}