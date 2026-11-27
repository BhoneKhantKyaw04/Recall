package com.example.recall.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recall.data.RecallRepository
import com.example.recall.domain.Scheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class DayBar(val label: String, val count: Int, val isToday: Boolean)

data class ProgressUiState(
    val totalCards: Int = 0,
    val knownCards: Int = 0,
    val studiedToday: Int = 0,
    val correctToday: Int = 0,
    val week: List<DayBar> = emptyList()
) {
    val accuracyToday: Int?
        get() = if (studiedToday == 0) null else (correctToday * 100) / studiedToday
}

class ProgressViewModel(repository: RecallRepository) : ViewModel() {
    private val today = Scheduler.currentDay()
    private val dayNames = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val uiState: StateFlow<ProgressUiState> = combine(
        repository.totalCount,
        repository.knownCount,
        repository.recentStats
    ) { total, known, stats ->
        val byDay = stats.associateBy { it.day }
        // Day 0 was a Thursday, so (day + 3) % 7 gives Monday = 0
        val week = (6 downTo 0).map { back ->
            val day = today - back
            DayBar(
                label = if (back == 0) "Today" else dayNames[((day + 3) % 7).toInt()],
                count = byDay[day]?.cardsStudied ?: 0,
                isToday = back == 0
            )
        }
        val todayStat = byDay[today]
        ProgressUiState(
            totalCards = total,
            knownCards = known,
            studiedToday = todayStat?.cardsStudied ?: 0,
            correctToday = todayStat?.correct ?: 0,
            week = week
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProgressUiState())
}