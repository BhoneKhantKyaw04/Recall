package com.example.recall.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recall.data.RecallRepository
import com.example.recall.data.SettingsRepository
import com.example.recall.domain.Scheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class HomeUiState(
    val dueCount: Int = 0,
    val studiedToday: Int = 0,
    val dailyLimit: Int = 10,
    val dueByDeck: Map<String, Int> = emptyMap()
) {
    val remaining: Int get() = maxOf(0, dailyLimit - studiedToday)
    val toStudy: Int get() = minOf(dueCount, remaining)
}

class HomeViewModel(
    repository: RecallRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val today = Scheduler.currentDay()

    val uiState: StateFlow<HomeUiState> = combine(
        repository.dueCount(today),
        repository.statsForDay(today),
        settingsRepository.settings,
        repository.dueByDeck(today)
    ) { due, stat, settings, decks ->
        HomeUiState(
            dueCount = due,
            studiedToday = stat?.cardsStudied ?: 0,
            dailyLimit = settings.dailyLimit,
            dueByDeck = decks.associate { it.deck to it.dueCount }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())
}