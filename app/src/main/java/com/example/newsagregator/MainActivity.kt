package com.example.newsagregator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.newsagregator.NewsApp
import com.example.newsagregator.ui.navigation.InitNavigation
import com.example.newsagregator.ui.theme.NewsAgregatorTheme
import com.example.newsagregator.ui.viewmodels.NewViewmodelFactory
import com.example.newsagregator.ui.viewmodels.NewsFeedViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewmodelFactory: NewViewmodelFactory

    private val viewModel: NewsFeedViewModel by viewModels { viewmodelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as NewsApp).appComponent.inject(this)
        enableEdgeToEdge()
        setContent {
            NewsAgregatorTheme {
                InitNavigation(viewModel)
            }
        }
    }
}