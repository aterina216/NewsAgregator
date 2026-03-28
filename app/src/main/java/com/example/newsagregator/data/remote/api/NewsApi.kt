package com.example.newsagregator.data.remote.api

import com.example.newsagregator.data.remote.KEY.API_KEY
import com.example.newsagregator.data.remote.dto.NewsResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getActualNews(
        @Query("country") country: String = "us",
        @Query("category") category: String = "general",
        @Query("api_key") api_Key: String = API_KEY,
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int
    ): Response<NewsResponseDTO>
}