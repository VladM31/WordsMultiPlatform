package vm.words.ua.exercise.domain.models.data

data class StartExerciseTransaction(
    val transactionId : String,
    val exercises : List<Int>,
    val words : List<Word>,
    val createdAt : Long
){

    data class Word(
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