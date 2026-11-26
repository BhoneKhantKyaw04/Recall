package com.example.recall

import android.content.Context
import androidx.room.Room
import com.example.recall.data.RecallDatabase
import com.example.recall.data.RecallRepository

class AppContainer(context: Context) {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        RecallDatabase::class.java,
        "recall.db"
    ).build()

    val repository = RecallRepository(
        database.flashcardDao(),
        database.dailyStatDao()
    )
}