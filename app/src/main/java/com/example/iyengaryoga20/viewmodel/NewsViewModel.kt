package com.example.iyengaryoga20.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewsViewModel : ViewModel() {

    private val _newsList = MutableStateFlow<List<com.example.iyengaryoga20.model.NewsItem>>(emptyList())
    val newsList = _newsList.asStateFlow()

    init {
        loadMockNews()
    }

    private fun loadMockNews() {
        _newsList.value = listOf(
            com.example.iyengaryoga20.model.NewsItem(
                id = "1",
                title = "Изменения в расписании на праздники",
                date = "25 дек",
                description = "Дорогие ученики! В период новогодних праздников студия работает по сокращенному графику. Успевайте записаться!",
                category = "Важно"
            ),
            com.example.iyengaryoga20.model.NewsItem(
                id = "2",
                title = "Мастер-класс: Здоровая спина",
                date = "28 дек",
                description = "Приглашаем на углубленный семинар по работе с поясничным отделом. Подходит для всех уровней подготовки.",
                category = "Событие"
            ),
            com.example.iyengaryoga20.model.NewsItem(
                id = "3",
                title = "Скидка 10% на абонементы",
                date = "30 дек",
                description = "Только до конца года при продлении абонемента вы получаете скидку и заморозку на 2 недели в подарок.",
                category = "Акция"
            ),
            com.example.iyengaryoga20.model.NewsItem(
                id = "4",
                title = "Новый преподаватель: Анна",
                date = "05 янв",
                description = "Рады приветствовать в нашей команде нового сертифицированного преподавателя метода Айенгара.",
                category = "Команда"
            )
        )
    }
}