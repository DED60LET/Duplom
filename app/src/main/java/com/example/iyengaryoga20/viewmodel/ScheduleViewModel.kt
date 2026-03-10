package com.example.iyengaryoga20.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.iyengaryoga20.data.RetrofitClient
import com.example.iyengaryoga20.data.SettingsManager
import com.example.iyengaryoga20.data.db.AppDatabase
import com.example.iyengaryoga20.data.db.BookingEntity
import com.example.iyengaryoga20.model.MobiClassElement
import com.example.iyengaryoga20.model.Teacher
import com.example.iyengaryoga20.model.YogaClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// ... FilterState остается без изменений ...
data class FilterState(
    val selectedHall: String = "Все залы",
    val selectedTypes: Set<String> = emptySet(),
    val selectedTeachers: Set<String> = emptySet(),
    val selectedDurations: Set<Int> = emptySet()
)

@OptIn(ExperimentalCoroutinesApi::class)
class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val settings = SettingsManager(application)

    private val currentUserPhone = settings.currentUserPhone
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _apiClasses = MutableStateFlow<List<YogaClass>>(emptyList())
    val allClasses = _apiClasses.asStateFlow()

    private val _localBookings = currentUserPhone.flatMapLatest { phone ->
        if (phone != null) db.bookingDao().getUserBookings(phone)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _selectedDate = MutableStateFlow(java.time.LocalDate.now())
    val selectedDate = _selectedDate.asStateFlow()

    private val _filters = MutableStateFlow(FilterState())
    val filters = _filters.asStateFlow()

    private val clubs = mapOf("5042" to "Вайнера", "5041" to "Антей", "6950" to "Куйбышева")

    val uiClasses = combine(_apiClasses, _localBookings, _selectedDate, _filters) { apiList, bookings, date, filters ->
        val mergedList = apiList.map { yogaClass ->
            val isBookedLocally = bookings.any { it.classId == yogaClass.id }
            if (isBookedLocally) yogaClass.copy(isUserBooked = true) else yogaClass
        }

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
        loadRealData()
    }

    fun toggleBooking(classId: String) {
        val phone = currentUserPhone.value ?: return
        val targetClass = _apiClasses.value.find { it.id == classId } ?: return

        viewModelScope.launch {
            val isBooked = db.bookingDao().isBooked(phone, classId)
            if (isBooked) {
                db.bookingDao().deleteBookingByClassId(classId, phone)
            } else {
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

    // --- ОБНОВЛЕННАЯ ЗАГРУЗКА ---
    private fun loadRealData() {
        Log.d("YOGA_APP", "Start loading data (Generic V6)...")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Берем дату из системы
                val today = java.time.LocalDate.now()
                val endDate = today.plusDays(14)
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

                val startStr = today.format(formatter)
                val endStr = endDate.format(formatter)

                Log.d("YOGA_APP", "Query dates: $startStr to $endStr")

                val deferreds = clubs.keys.map { clubId ->
                    async {
                        try {
                            // ИЗМЕНЕНИЕ: Убран параметр widgetCode
                            val response = RetrofitClient.api.getSchedule(
                                clubId = clubId,
                                startDate = startStr,
                                endDate = endStr
                            )
                            Log.d("YOGA_APP", "Club $clubId success: ${response.size} items")
                            response.map { it to clubs[clubId] }
                        } catch (e: Exception) {
                            Log.e("YOGA_APP", "Error loading club $clubId: ${e.message}")
                            emptyList<Pair<MobiClassElement, String?>>()
                        }
                    }
                }

                val results = deferreds.awaitAll().flatten()
                val mappedClasses = results.mapNotNull { (mobi, club) -> mapMobiToYogaClass(mobi, club ?: "") }

                _apiClasses.value = mappedClasses
                Log.d("YOGA_APP", "Total loaded: ${mappedClasses.size}")

            } catch (e: Exception) {
                Log.e("YOGA_APP", "Global loading error: ${e.message}")
            }
        }
    }

    private fun mapMobiToYogaClass(mobi: MobiClassElement, clubName: String): YogaClass? {
        try {
            if (mobi.start == null || mobi.end == null) return null
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val startDateTime = LocalDateTime.parse(mobi.start, formatter)
            val endDateTime = LocalDateTime.parse(mobi.end, formatter)
            val duration = java.time.Duration.between(startDateTime, endDateTime).toMinutes().toInt()

            val realTitle = mobi.title ?: mobi.name ?: "Занятие"
            val realType = mobi.activity?.title ?: "Общий класс"
            val realTeacher = mobi.teacher?.name ?: "Инструктор"

            return YogaClass(
                id = mobi.id ?: java.util.UUID.randomUUID().toString(),
                title = realTitle,
                type = realType,
                teacher = Teacher(realTeacher),
                startTime = startDateTime,
                durationMinutes = duration,
                totalSpots = mobi.availableSlots ?: 20,
                bookedSpots = 0,
                hall = clubName
            )
        } catch (e: Exception) { return null }
    }

    // Фильтры
    fun selectDate(date: java.time.LocalDate) { _selectedDate.value = date }
    fun setHall(hall: String) { _filters.value = _filters.value.copy(selectedHall = hall) }
    fun toggleTypeFilter(t: String) {
        val s = _filters.value.selectedTypes.toMutableSet(); if(s.contains(t)) s.remove(t) else s.add(t); _filters.value = _filters.value.copy(selectedTypes = s)
    }
    fun toggleTeacherFilter(n: String) {
        val s = _filters.value.selectedTeachers.toMutableSet(); if(s.contains(n)) s.remove(n) else s.add(n); _filters.value = _filters.value.copy(selectedTeachers = s)
    }
    fun toggleDurationFilter(d: Int) {
        val s = _filters.value.selectedDurations.toMutableSet(); if(s.contains(d)) s.remove(d) else s.add(d); _filters.value = _filters.value.copy(selectedDurations = s)
    }
    fun clearFilters() { _filters.value = FilterState() }
}