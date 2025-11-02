package vm.words.ua.exercise.net.responds

import kotlinx.serialization.Serializable

@Serializable
data class StartExerciseTransactionRequest(
    val transactionId: String,
    val exercises: List<Int>,
    val words: List<WordRequest>,
    val createdAt: Long,
    val wordCount: WordCountRequest,
    val learningPlan: LearningPlan?
) {

    @Serializable
    data class WordCountRequest(
        val addedToLearning: Int,
        val repetitions: Int,
    )

    @Serializable
    data class LearningPlan(
        val wordsPerDay: Int,
        val nativeLang: String,
        val learningLang: String,
        val cefr: String,
        val dateOfCreation: String
    )

    @Serializable
    data class WordRequest(

        val wordId: String,
        val userWordId: String,
        val grade: Int,
        val dateOfAdded: String,
        val lastReadDate: String,
        val original: String,
        val translate: String,
        val lang: String,
        val translateLang: String,
        val cefr: String,
        val hasDescription: Boolean,
        val category: String?,
        val hasSound: Boolean,
        val hasImage: Boolean
    )
}


