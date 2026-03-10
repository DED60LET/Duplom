package com.example.iyengaryoga20.model

data class NewsItem(
    val id: String,
    val title: String,
    val date: String,
    val description: String,
    val category: String = "Студия" // Например: "Акция", "Событие", "Студия"
)