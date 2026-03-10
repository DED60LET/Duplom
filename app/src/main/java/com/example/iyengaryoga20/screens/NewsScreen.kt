package com.example.iyengaryoga20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iyengaryoga20.ui.components.NewsCard
import com.example.iyengaryoga20.viewmodel.NewsViewModel

@Composable
fun NewsScreen(
    viewModel: NewsViewModel = viewModel()
) {
    val newsList by viewModel.newsList.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            // ЦВЕТ: Фон теперь берется из темы (Серый, Темный или Лавандовый)
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Новости и Акции",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground, // Цвет текста
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(newsList) { news ->
                NewsCard(newsItem = news)
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}