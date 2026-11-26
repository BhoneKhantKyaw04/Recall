package com.example.recall.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Flashcard::class, DailyStat::class],
    version = 1,
    exportSchema = false
)
abstract class RecallDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao
    abstract fun dailyStatDao(): DailyStatDao
}