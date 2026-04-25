package com.example.iyengaryoga20.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iyengaryoga20.model.NewsItem
import com.example.iyengaryoga20.ui.components.NewsCard
import com.example.iyengaryoga20.viewmodel.NewsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel = viewModel()
) {
    val newsList by viewModel.newsList.collectAsState()

    // --- ПЕРЕМЕННЫЕ ДЛЯ ВСПЛЫВАЮЩЕГО ОКНА ---
    var selectedNews by remember { mutableStateOf<NewsItem?>(null) } // Какая новость выбрана
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true) // Состояние шторки

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Новости и Акции",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(newsList) { news ->
                NewsCard(
                    newsItem = news,
                    onReadMoreClick = {
                        selectedNews = news // При клике сохраняем новость, окно откроется
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    // --- ВСПЛЫВАЮЩЕЕ ОКНО (ШТОРКА) ---
    if (selectedNews != null) {
        ModalBottomSheet(
            onDismissRequest = { selectedNews = null }, // При клике мимо окна оно закрывается
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()) // Скролл для длинного текста
            ) {
                // Тег и Дата
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = selectedNews!!.category.uppercase(),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = selectedNews!!.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Заголовок
                Text(
                    text = selectedNews!!.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Полный текст
                Text(
                    text = selectedNews!!.fullText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Кнопка закрытия окна
                Button(
                    onClick = { selectedNews = null },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Закрыть", color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.padding(8.dp))
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}