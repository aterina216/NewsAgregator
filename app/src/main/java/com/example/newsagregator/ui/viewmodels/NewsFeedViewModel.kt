package com.example.newsagregator.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.room.util.query
import com.example.newsagregator.data.repository.NewsRepositoryImpl
import com.example.newsagregator.domain.models.Article
import com.example.newsagregator.domain.repository.NewsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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

    private var _favoriteArticles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val favoriteArticles: StateFlow<PagingData<Article>> = _favoriteArticles.asStateFlow()

    private var _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private var _historyArticles = MutableStateFlow<PagingData<Article>>(PagingData.empty())
    val historyArticles: StateFlow<PagingData<Article>> = _historyArticles.asStateFlow()

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
        loadFavorites()
        loadHistory()
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

    fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavoriteArticles().collectLatest {
                pagingData ->
                _favoriteArticles.value = pagingData
            }
        }
    }

    fun toggleFavorite(article: Article, isFavorite: Boolean) {
        viewModelScope.launch {
            if(isFavorite) {
                repository.removeFromFavorites(article)
            }
            else {
                repository.addToFavorites(article)
            }
        }
    }

    fun getArticleFavoriteState(url: String): Flow<Boolean> {
        return repository.getArticleFavoriteState(url)
    }

    fun loadFavoriteStatus(url: String) {
        viewModelScope.launch {
            repository.getArticleFavoriteState(url).collect {
                favorite ->
                _isFavorite.value = favorite
            }
        }
    }

    suspend fun updateViewAt(viewAt: Long, url: String) {
        viewModelScope.launch {
            try {
                repository.updateViewAt(viewAt, url)
            }
            catch (e: Exception) {
                Log.d("viewmodel", "${e.message}")
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            repository.getHistoryArticles().cachedIn(viewModelScope).collectLatest {
                pagingData -> _historyArticles.value = pagingData
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            loadHistory()
        }
    }
}