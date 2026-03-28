package com.example.newsagregator.di

import com.example.newsagregator.data.repository.NewsRepositoryImpl
import com.example.newsagregator.domain.repository.NewsRepository
import com.example.newsagregator.ui.viewmodels.NewViewmodelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelFactoryModule {

    @Provides
    @Singleton
    fun provideViewModelFactory(repo: NewsRepositoryImpl): NewViewmodelFactory {
        return NewViewmodelFactory(repo)
    }
}