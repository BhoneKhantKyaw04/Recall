package com.example.recall.domain

import com.example.recall.data.Flashcard
import java.util.TimeZone

object Scheduler {
    const val MAX_BOX = 5

    // Days until the next review for each box
    private val intervalDays = mapOf(1 to 1, 2 to 2, 3 to 4, 4 to 7, 5 to 14)

    /** Today's day number in the device's time zone. */
    fun currentDay(nowMillis: Long = System.currentTimeMillis()): Long {
        val offset = TimeZone.getDefault().getOffset(nowMillis)
        return (nowMillis + offset) / 86_400_000L
    }

    /**
     * Updates a card after the student answers.
     * Right: move up one box. Wrong: move down one box (never below 1)
     * and see it again tomorrow. No penalties or lost progress beyond that.
     */
    fun review(card: Flashcard, correct: Boolean, today: Long): Flashcard {
        return if (correct) {
            val newBox = minOf(card.box + 1, MAX_BOX)
            card.copy(
                box = newBox,
                nextReviewDay = today + intervalDays.getValue(newBox),
                timesCorrect = card.timesCorrect + 1
            )
        } else {
            card.copy(
                box = maxOf(card.box - 1, 1),
                nextReviewDay = today + 1,
                timesWrong = card.timesWrong + 1
            )
        }
    }
}