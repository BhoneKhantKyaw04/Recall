package com.example.recall.data

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query

data class TriviaResponse(
    @SerializedName("response_code") val responseCode: Int,
    val results: List<TriviaQuestion> = emptyList()
)

data class TriviaQuestion(
    val question: String,
    @SerializedName("correct_answer") val correctAnswer: String
)

interface TriviaApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int,
        @Query("difficulty") difficulty: String = "easy",
        @Query("type") type: String = "multiple",
        @Query("encode") encode: String = "url3986"
    ): TriviaResponse
}

sealed interface DownloadResult {
    data class Added(val count: Int) : DownloadResult
    data object NothingNew : DownloadResult
    data class Failed(val reason: String) : DownloadResult
}