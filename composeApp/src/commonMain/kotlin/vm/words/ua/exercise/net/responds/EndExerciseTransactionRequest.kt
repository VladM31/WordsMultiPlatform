package vm.words.ua.exercise.net.responds

import kotlinx.serialization.Serializable

@Serializable
data class EndExerciseTransactionRequest(
    val transactionId: String,
    var endedAt: Long
)