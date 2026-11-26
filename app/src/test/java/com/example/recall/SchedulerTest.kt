package com.example.recall

import com.example.recall.data.Flashcard
import com.example.recall.domain.Scheduler
import org.junit.Assert.assertEquals
import org.junit.Test

class SchedulerTest {

    private fun card(box: Int = 1) =
        Flashcard(deck = "Test", question = "Q", answer = "A", box = box)

    @Test
    fun correctAnswer_movesUpOneBox_andSchedulesLater() {
        val result = Scheduler.review(card(box = 1), correct = true, today = 100)
        assertEquals(2, result.box)
        assertEquals(102L, result.nextReviewDay)
        assertEquals(1, result.timesCorrect)
    }

    @Test
    fun correctAnswer_inTopBox_staysInTopBox() {
        val result = Scheduler.review(card(box = 5), correct = true, today = 100)
        assertEquals(5, result.box)
        assertEquals(114L, result.nextReviewDay)
    }

    @Test
    fun wrongAnswer_movesDownOneBox_andIsDueTomorrow() {
        val result = Scheduler.review(card(box = 3), correct = false, today = 100)
        assertEquals(2, result.box)
        assertEquals(101L, result.nextReviewDay)
        assertEquals(1, result.timesWrong)
    }

    @Test
    fun wrongAnswer_inBoxOne_neverGoesBelowOne() {
        val result = Scheduler.review(card(box = 1), correct = false, today = 100)
        assertEquals(1, result.box)
    }
}