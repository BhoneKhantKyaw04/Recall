package com.example.recall.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flashcards")
data class Flashcard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deck: String,
    val question: String,
    val answer: String,
    val box: Int = 1,               // Leitner box 1 (new) to 5 (known well)
    val nextReviewDay: Long = 0,    // day number, due when <= today
    val timesCorrect: Int = 0,
    val timesWrong: Int = 0
)

@Entity(tableName = "daily_stats")
data class DailyStat(
    @PrimaryKey val day: Long,      // day number
    val cardsStudied: Int = 0,
    val correct: Int = 0
)

data class DeckCount(val deck: String, val dueCount: Int)