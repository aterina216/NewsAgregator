package com.example.newsagregator.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.room.util.query
import com.example.newsagregator.data.repository.NewsRepositoryImpl
import com.example.newsagregator.domain.models.Article
import com.example.newsagregator.domain.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class NewsFeedViewModel(private val repository: NewsRepository): ViewModel() {

    private val _selectedCategory = MutableStateFlow("general")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private var _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private var _currentArticle = MutableStateFlow<Article?>(null)
    val currentArticle = _currentArticle

    private var _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    @OptIn(ExperimentalCoroutinesApi::class)
    val news = _selectedCategory.flatMapLatest {
        category ->
        Log.d("ViewModel", "Загружаем категорию: $category")
        repository.getNews(category).cachedIn(viewModelScope)
    }

    val searchResults = _searchQuery
        .debounce(800)
        .filter { it.isNotBlank() && it.length >= 3 }
        .flatMapLatest { query ->
            repository.searchNews(query).cachedIn(viewModelScope)
        }

    fun selectedCategory(category: String) {
        Log.d("ViewModel", "Выбрана категория: $category")
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }

    init {
        Log.d("ViewModel", "NewsFeedViewModel создан")
    }

    fun getArticleByUrl(url: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _currentArticle.value = repository.getArticleByUrl(url)
                _isLoading.value = false
            }
            catch (e: Exception) {
                _isLoading.value = false
                Log.e("ViewModel", "${e.stackTrace}")
            }
        }
    }
}