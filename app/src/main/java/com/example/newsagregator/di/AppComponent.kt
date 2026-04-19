package com.example.newsagregator.di

import android.content.Context
import com.example.newsagregator.MainActivity
import com.example.newsagregator.NewsApp
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class, RepositoryModule::class, ViewModelFactoryModule::class, ThemePrefsModule::class])
interface AppComponent {

    fun  inject(app: NewsApp)
    fun inject(activity: MainActivity)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder
        fun build(): AppComponent
    }
}