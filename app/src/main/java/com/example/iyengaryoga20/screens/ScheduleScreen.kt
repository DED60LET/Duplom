package com.example.iyengaryoga20.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.iyengaryoga2.ui.components.CalendarStrip
import com.example.iyengaryoga20.components.YogaCardItem
import com.example.iyengaryoga20.viewmodel.ScheduleViewModel
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(viewModel: ScheduleViewModel) {
    // 1. Подписываемся на данные из ViewModel (версия с API и БД)
    val classes by viewModel.uiClasses.collectAsState(initial = emptyList())
    val selectedDate by viewModel.selectedDate.collectAsState()
    val filters by viewModel.filters.collectAsState() // Здесь лежит selectedHall

    // Списки для фильтров
    val allTypes = remember { listOf("Начальный", "Терапия", "Основной", "Релакс") }
    val allTeachers = remember { listOf("Елена Смирнова", "Игорь Ветров", "Анна Каренина") }
    val allDurations = remember { listOf(45, 60, 90) }

    // Управление шторкой
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val dateFormatter = DateTimeFormatter.ofPattern("d MMMM", Locale("ru"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            // Цвет фона берем из темы
            .background(MaterialTheme.colorScheme.background)
    ) {
        // --- ШАПКА ---
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp)) {
            Text(text = "Iyengar Yoga", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Расписание", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)

                // Кнопка Фильтров
                FilledTonalIconButton(
                    onClick = { showFilterSheet = true },
                    colors = IconButtonDefaults.filledTonalIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Icon(Icons.Default.FilterList, contentDescription = "Фильтры", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // --- ВЫБОР ЗАЛА (Филиала) ---
        Spacer(modifier = Modifier.height(12.dp))
        HallSelector(
            // Используем filters.selectedHall вместо старого selectedClubName
            selectedHall = filters.selectedHall,
            onHallSelected = { viewModel.setHall(it) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- КАЛЕНДАРЬ ---
        CalendarStrip(
            selectedDate = selectedDate,
            onDateSelected = { viewModel.selectDate(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- ДАТА ---
        Text(
            text = selectedDate.format(dateFormatter),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- СПИСОК ЗАНЯТИЙ ---
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            if (classes.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Нет занятий",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(classes) { yogaClass ->
                    YogaCardItem(
                        yogaClass = yogaClass,
                        onButtonClick = { viewModel.toggleBooking(yogaClass.id) }
                    )
                }
            }
            // Отступ под меню
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }

    // --- BOTTOM SHEET (Шторка фильтров) ---
    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = { showFilterSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Фильтры", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Spacer(modifier = Modifier.height(16.dp))

                FilterSectionTitle("Тип занятия")
                FlowRowChipGroup(
                    items = allTypes,
                    selectedItems = filters.selectedTypes,
                    onItemClick = { viewModel.toggleTypeFilter(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                FilterSectionTitle("Преподаватель")
                FlowRowChipGroup(
                    items = allTeachers,
                    selectedItems = filters.selectedTeachers,
                    onItemClick = { viewModel.toggleTeacherFilter(it) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                FilterSectionTitle("Длительность")
                FlowRowChipGroup(
                    items = allDurations.map { "$it мин" },
                    selectedItems = filters.selectedDurations.map { "$it мин" }.toSet(),
                    onItemClick = { str ->
                        val duration = str.replace(" мин", "").toInt()
                        viewModel.toggleDurationFilter(duration)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { viewModel.clearFilters() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Сбросить", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Button(
                        onClick = { showFilterSheet = false },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Применить", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// --- ВСПОМОГАТЕЛЬНЫЕ КОМПОНЕНТЫ ---

@Composable
fun HallSelector(
    selectedHall: String,
    onHallSelected: (String) -> Unit
) {
    val halls = listOf("Все залы", "Вайнера", "Антей", "Куйбышева")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(halls) { hall ->
            val isSelected = hall == selectedHall
            Surface(
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(20.dp),
                border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha=0.3f)),
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onHallSelected(hall) }
            ) {
                Text(
                    text = hall,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }
    }
}

@Composable
fun FilterSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRowChipGroup(
    items: List<String>,
    selectedItems: Set<String>,
    onItemClick: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            val isSelected = selectedItems.contains(item)
            FilterChip(
                selected = isSelected,
                onClick = { onItemClick(item) },
                label = { Text(item) },
                enabled = true,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSurface,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha=0.5f),
                    selectedBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}