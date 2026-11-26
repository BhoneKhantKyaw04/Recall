package com.example.recall.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcards WHERE nextReviewDay <= :today ORDER BY box, id LIMIT :limit")
    suspend fun getDueCards(today: Long, limit: Int): List<Flashcard>

    @Query("SELECT COUNT(*) FROM flashcards WHERE nextReviewDay <= :today")
    fun observeDueCount(today: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM flashcards")
    fun observeTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM flashcards WHERE box >= 4")
    fun observeKnownCount(): Flow<Int>

    @Insert
    suspend fun insertAll(cards: List<Flashcard>)

    @Update
    suspend fun update(card: Flashcard)

    @Query("SELECT COUNT(*) FROM flashcards")
    suspend fun count(): Int

    @Query("SELECT question FROM flashcards")
    suspend fun allQuestions(): List<String>
}


@Dao
interface DailyStatDao {
    @Query("SELECT * FROM daily_stats WHERE day = :day")
    suspend fun get(day: Long): DailyStat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(stat: DailyStat)

    @Query("SELECT * FROM daily_stats ORDER BY day DESC LIMIT 7")
    fun observeRecent(): Flow<List<DailyStat>>

    @Query("SELECT * FROM daily_stats WHERE day = :day")
    fun observeDay(day: Long): Flow<DailyStat?>

}