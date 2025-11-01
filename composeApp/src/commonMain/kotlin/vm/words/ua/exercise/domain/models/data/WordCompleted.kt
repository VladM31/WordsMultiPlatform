package vm.words.ua.exercise.domain.models.data

data class WordCompleted(
    val transactionId : String,
    val wordId: String,
    val userWordId: String,
    val exerciseId: Int,
    val attempts: Int,
    val isCorrect: Boolean,
    val completedAt: Long
)
