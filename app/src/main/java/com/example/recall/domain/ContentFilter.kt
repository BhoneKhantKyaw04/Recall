package com.example.recall.domain

object ContentFilter {
    private val needsOptions = listOf("following", "of these", "which of", "the above", "true or false")

    fun isStandalone(question: String): Boolean =
        needsOptions.none { question.contains(it, ignoreCase = true) }
}