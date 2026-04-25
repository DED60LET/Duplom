package com.example.iyengaryoga20.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iyengaryoga20.data.SettingsManager
import com.example.iyengaryoga20.data.db.AppDatabase
import com.example.iyengaryoga20.data.db.BookingEntity
import com.example.iyengaryoga20.model.Teacher
import com.example.iyengaryoga20.model.YogaClass
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID

// Фильтры
data class FilterState(
    val selectedHall: String = "Вайнера", // По умолчанию Вайнера
    val selectedTypes: Set<String> = emptySet(),
    val selectedTeachers: Set<String> = emptySet(),
    val selectedDurations: Set<Int> = emptySet()
)

class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val settings = SettingsManager(application)

    // Текущий авторизованный пользователь
    private val currentUserPhone = settings.currentUserPhone
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    // Наше нативное расписание
    private val _apiClasses = MutableStateFlow<List<YogaClass>>(emptyList())
    val allClasses = _apiClasses.asStateFlow()

    // Локальные записи пользователя (Читаем напрямую из БД)
    val localBookings = currentUserPhone.flatMapLatest { phone ->
        val safePhone = phone ?: "+79991112233" // Предохранитель
        db.bookingDao().getUserBookings(safePhone)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _filters = MutableStateFlow(FilterState())
    val filters = _filters.asStateFlow()

    // --- ОБЪЕДИНЕНИЕ РАСПИСАНИЯ И ЗАПИСЕЙ ---
    val uiClasses = combine(_apiClasses, localBookings, _selectedDate, _filters) { apiList, bookings, date, filters ->

        // 1. Проставляем галочки "Записан" из базы данных
        val mergedList = apiList.map { yogaClass ->
            val isBookedLocally = bookings.any { it.classId == yogaClass.id }
            if (isBookedLocally) yogaClass.copy(isUserBooked = true) else yogaClass.copy(isUserBooked = false)
        }

        // 2. Фильтруем (Даты, Залы, Учителя)
        mergedList.filter { yogaClass ->
            val dateMatch = yogaClass.startTime.toLocalDate().isEqual(date)
            val hallMatch = filters.selectedHall == "Все залы" || yogaClass.hall == filters.selectedHall
            val typeMatch = filters.selectedTypes.isEmpty() || yogaClass.type in filters.selectedTypes
            val teacherMatch = filters.selectedTeachers.isEmpty() || yogaClass.teacher.name in filters.selectedTeachers
            val durationMatch = filters.selectedDurations.isEmpty() || yogaClass.durationMinutes in filters.selectedDurations

            dateMatch && hallMatch && typeMatch && teacherMatch && durationMatch
        }.sortedBy { it.startTime }
    }

    init {
        loadMockData() // Запускаем реалистичную симуляцию при старте
    }

    // --- ЛОГИКА ЗАПИСИ (Сохраняем в телефон) ---
    fun toggleBooking(classId: String) {
        val phone = currentUserPhone.value ?: "+79991112233"
        val targetClass = _apiClasses.value.find { it.id == classId } ?: return

        viewModelScope.launch {
            val isBooked = db.bookingDao().isBooked(phone, classId)

            if (isBooked) {
                // ОТМЕНА ЗАПИСИ
                db.bookingDao().deleteBookingByClassId(classId, phone)
            } else {
                // СОЗДАНИЕ ЗАПИСИ В БД
                val newBooking = BookingEntity(
                    userPhone = phone,
                    classId = targetClass.id,
                    className = targetClass.title,
                    classType = targetClass.type,
                    teacherName = targetClass.teacher.name,
                    date = targetClass.startTime.toLocalDate().toString(),
                    startTime = targetClass.startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                )
                db.bookingDao().insertBooking(newBooking)
            }
        }
    }

    // Удаление записи (для экрана "Мои записи")
    fun deleteBooking(classId: String) {
        val phone = currentUserPhone.value ?: "+79991112233"
        viewModelScope.launch {
            db.bookingDao().deleteBookingByClassId(classId, phone)
        }
    }

    // Фильтры
    fun selectDate(date: LocalDate) { _selectedDate.value = date }
    fun setHall(hall: String) { _filters.value = _filters.value.copy(selectedHall = hall) }
    fun toggleTypeFilter(type: String) {
        val s = _filters.value.selectedTypes.toMutableSet(); if(s.contains(type)) s.remove(type) else s.add(type)
        _filters.value = _filters.value.copy(selectedTypes = s)
    }
    fun toggleTeacherFilter(name: String) {
        val s = _filters.value.selectedTeachers.toMutableSet(); if(s.contains(name)) s.remove(name) else s.add(name)
        _filters.value = _filters.value.copy(selectedTeachers = s)
    }
    fun toggleDurationFilter(d: Int) {
        val s = _filters.value.selectedDurations.toMutableSet(); if(s.contains(d)) s.remove(d) else s.add(d)
        _filters.value = _filters.value.copy(selectedDurations = s)
    }
    fun clearFilters() { _filters.value = FilterState() }

    // --- РЕАЛИСТИЧНАЯ СИМУЛЯЦИЯ РАСПИСАНИЯ ---
    private fun loadMockData() {
        val today = LocalDate.now()
        val list = mutableListOf<YogaClass>()

        // Генерируем расписание на 14 дней
        for (i in 0..14) {
            val d = today.plusDays(i.toLong())

            // ВАЙНЕРА
            list.add(YogaClass(UUID.randomUUID().toString(), "Совместная практика (Level 1)", "Начальный", Teacher("Елена Смирнова"), d.atTime(7, 30), 90, 15, 5, false, "Вайнера"))
            list.add(YogaClass(UUID.randomUUID().toString(), "Здоровая спина", "Терапия", Teacher("Игорь Ветров"), d.atTime(18, 30), 60, 12, 12, false, "Вайнера"))

            // АНТЕЙ
            list.add(YogaClass(UUID.randomUUID().toString(), "Женская практика", "Основной", Teacher("Анна Каренина"), d.atTime(10, 0), 90, 20, 10, false, "Антей"))
            list.add(YogaClass(UUID.randomUUID().toString(), "Медитация", "Релакс", Teacher("Игорь Ветров"), d.atTime(20, 0), 45, 10, 2, false, "Антей"))

            // КУЙБЫШЕВА
            list.add(YogaClass(UUID.randomUUID().toString(), "Йога Айенгара", "Продвинутый", Teacher("Елена Смирнова"), d.atTime(19, 0), 90, 15, 14, false, "Куйбышева"))
        }
        _apiClasses.value = list
    }
}