package vm.words.ua.exercise.net.responds

import kotlinx.serialization.Serializable

@Serializable
data class WordCompletedRequest(
    val transactionId: String,
    val wordId: String,
    val userWordId: String,
    val exerciseId: Int,
    val attempts: Int,
    val isCorrect: Boolean,
    val completedAt: Long
)
