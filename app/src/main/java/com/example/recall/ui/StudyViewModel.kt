package com.example.recall.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recall.data.Flashcard
import com.example.recall.data.RecallRepository
import com.example.recall.data.SettingsRepository
import com.example.recall.domain.Scheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudyUiState(
    val loading: Boolean = true,
    val currentCard: Flashcard? = null,
    val showAnswer: Boolean = false,
    val answered: Int = 0,
    val correct: Int = 0,
    val total: Int = 0,
    val finished: Boolean = false
)

class StudyViewModel(
    private val repository: RecallRepository,
    private val settingsRepository: SettingsRepository,
    private val deck: String?
) : ViewModel() {

    private val today = Scheduler.currentDay()
    private var queue: List<Flashcard> = emptyList()
    private var index = 0

    private val _uiState = MutableStateFlow(StudyUiState())
    val uiState: StateFlow<StudyUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch { loadCards() }
    }

    private suspend fun loadCards() {
        val limit = settingsRepository.settings.value.dailyLimit
        val studiedToday = repository.statsForDay(today).first()?.cardsStudied ?: 0
        val remaining = maxOf(0, limit - studiedToday)
        queue = repository.getDueCards(today, remaining, deck)
        index = 0
        _uiState.value = StudyUiState(
            loading = false,
            currentCard = queue.firstOrNull(),
            total = queue.size,
            finished = queue.isEmpty()
        )
    }

    fun revealAnswer() {
        _uiState.update { it.copy(showAnswer = true) }
    }

    fun answer(correct: Boolean) {
        val card = _uiState.value.currentCard ?: return
        viewModelScope.launch {
            repository.recordAnswer(card, correct, today)
            index++
            _uiState.update {
                it.copy(
                    showAnswer = false,
                    answered = it.answered + 1,
                    correct = it.correct + if (correct) 1 else 0,
                    currentCard = queue.getOrNull(index),
                    finished = index >= queue.size
                )
            }
        }
    }
}