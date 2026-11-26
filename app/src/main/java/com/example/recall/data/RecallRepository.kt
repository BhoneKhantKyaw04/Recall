package com.example.recall.data

import com.example.recall.domain.ContentFilter
import com.example.recall.domain.Scheduler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException
import java.net.URLDecoder

class RecallRepository(
    private val cardDao: FlashcardDao,
    private val statDao: DailyStatDao,
    private val triviaApi: TriviaApi
) {
    val totalCount: Flow<Int> = cardDao.observeTotalCount()
    val knownCount: Flow<Int> = cardDao.observeKnownCount()
    val recentStats: Flow<List<DailyStat>> = statDao.observeRecent()

    // Deck name and Open Trivia DB category id (kept to school-friendly subjects)
    private val onlineTopics = listOf("Science" to 17, "Maths" to 19, "Geography" to 22)

    fun dueCount(today: Long): Flow<Int> = cardDao.observeDueCount(today)
    fun statsForDay(day: Long): Flow<DailyStat?> = statDao.observeDay(day)

    suspend fun getDueCards(today: Long, limit: Int, deck: String? = null): List<Flashcard> =
        if (deck == null) cardDao.getDueCards(today, limit)
        else cardDao.getDueCardsForDeck(deck, today, limit)

    fun dueByDeck(today: Long): Flow<List<DeckCount>> = cardDao.observeDueByDeck(today)

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

    /** Downloads extra questions. Only called when the student asks for it. */
    suspend fun downloadMoreCards(): DownloadResult {
        try {
            val known = cardDao.allQuestions().toHashSet()
            val newCards = mutableListOf<Flashcard>()

            onlineTopics.forEachIndexed { index, (deck, categoryId) ->
                // The service allows one request every 5 seconds
                if (index > 0) delay(5_500)
                val response = triviaApi.getQuestions(amount = 10, category = categoryId)
                if (response.responseCode == 5) {
                    return DownloadResult.Failed("The question service is busy. Please try again in a few seconds.")
                }
                response.results.forEach { item ->
                    val question = decode(item.question)
                    if (ContentFilter.isStandalone(question) && known.add(question)) {
                        newCards += Flashcard(deck = deck, question = question, answer = decode(item.correctAnswer))
                    }
                }
            }

            if (newCards.isEmpty()) return DownloadResult.NothingNew
            cardDao.insertAll(newCards)
            return DownloadResult.Added(newCards.size)
        } catch (e: IOException) {
            return DownloadResult.Failed("Could not connect. Check your internet and try again.")
        } catch (e: HttpException) {
            return DownloadResult.Failed("The question service had a problem. Please try again later.")
        }
    }

    private fun decode(text: String): String = URLDecoder.decode(text, "UTF-8")
}