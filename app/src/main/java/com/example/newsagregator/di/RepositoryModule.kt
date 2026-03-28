package com.example.newsagregator.di

import com.example.newsagregator.data.db.database.ArticleDataBase
import com.example.newsagregator.data.remote.api.NewsApi
import com.example.newsagregator.data.repository.NewsRepositoryImpl
import com.example.newsagregator.domain.repository.NewsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(api: NewsApi, dataBase: ArticleDataBase): NewsRepositoryImpl {
        return NewsRepositoryImpl(api, dataBase)
    }
}