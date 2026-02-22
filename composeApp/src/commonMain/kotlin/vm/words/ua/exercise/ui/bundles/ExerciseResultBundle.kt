package vm.words.ua.exercise.ui.bundles

import vm.words.ua.exercise.domain.models.enums.Exercise

data class ExerciseResultBundle(
    val wordResults: List<WordGradeResult>,
    val exercises: List<Exercise>,
) {
    val totalWords: Int get() = wordResults.map { it.wordId }.distinct().size
    val correctWords: Int
        get() = wordResults
            .groupBy { it.wordId }
            .count { (_, results) -> results.all { it.isCorrect } }
    val accuracy: Int
        get() = if (totalWords > 0) (correctWords * 100) / totalWords else 0
    val averageGrade: Float
        get() = if (wordResults.isEmpty()) 0f
        else wordResults.map { it.grade.toFloat() / it.maxGrade }.average().toFloat()
}

data class WordGradeResult(
    val wordId: String,
    val original: String,
    val translate: String,
    val grade: Int,
    val maxGrade: Int,
    val isCorrect: Boolean, // grade > 0
    val exerciseId: Int,
)

