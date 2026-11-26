package com.example.recall

import android.content.Context
import androidx.room.Room
import com.example.recall.data.RecallDatabase
import com.example.recall.data.RecallRepository
import com.example.recall.data.SettingsRepository
import com.example.recall.data.TriviaApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppContainer(context: Context) {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        RecallDatabase::class.java,
        "recall.db"
    ).build()

    private val triviaApi: TriviaApi = Retrofit.Builder()
        .baseUrl("https://opentdb.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TriviaApi::class.java)

    val repository = RecallRepository(
        database.flashcardDao(),
        database.dailyStatDao(),
        triviaApi
    )

    val settings = SettingsRepository(context.applicationContext)
}