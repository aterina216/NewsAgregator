package com.example.newsagregator.utils

import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date

object Format {
    fun formatDate(timestamp: Long): String {
        return try {
            val date = Date(timestamp)
            val format = java.text.SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
            format.format(date)
        } catch (e: Exception) {
            ""
        }
    }
}