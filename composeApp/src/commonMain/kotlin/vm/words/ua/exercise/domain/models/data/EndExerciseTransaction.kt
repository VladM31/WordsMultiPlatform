package vm.words.ua.exercise.domain.models.data

import kotlinx.datetime.Clock

data class EndExerciseTransaction(
    val transactionId: String,
    var endedAt: Long = Clock.System.now().toEpochMilliseconds()
)