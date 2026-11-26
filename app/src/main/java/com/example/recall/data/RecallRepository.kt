package com.example.recall.data

import com.example.recall.domain.Scheduler
import kotlinx.coroutines.flow.Flow

class RecallRepository(
    private val cardDao: FlashcardDao,
    private val statDao: DailyStatDao
) {
    val totalCount: Flow<Int> = cardDao.observeTotalCount()
    val knownCount: Flow<Int> = cardDao.observeKnownCount()
    val recentStats: Flow<List<DailyStat>> = statDao.observeRecent()

    fun dueCount(today: Long): Flow<Int> = cardDao.observeDueCount(today)
    fun statsForDay(day: Long): Flow<DailyStat?> = statDao.observeDay(day)

    suspend fun getDueCards(today: Long, limit: Int): List<Flashcard> =
        cardDao.getDueCards(today, limit)

    suspend fun recordAnswer(card: Flashcard, correct: Boolean, today: Long) {
        cardDao.update(Scheduler.review(card, correct, today))
        val current = statDao.get(today) ?: DailyStat(day = today)
        statDao.upsert(
            current.copy(
                cardsStudied = current.cardsStudied + 1,
                correct = current.correct + if (correct) 1 else 0
            )
        )
    }

    suspend fun seedIfEmpty() {
        if (cardDao.count() == 0) cardDao.insertAll(StarterDecks.cards)
    }
}