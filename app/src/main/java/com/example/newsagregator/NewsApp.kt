package com.example.newsagregator

import android.app.Application
import com.example.newsagregator.di.AppComponent
import com.example.newsagregator.di.DaggerAppComponent

class NewsApp: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder().context(this).build()
    }
}