package vm.words.ua.exercise.ui.bundles

import vm.words.ua.exercise.domain.models.data.ExerciseSelection
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise

data class ExerciseBundle(
    val number: Int = 0,
    val exercises: List<ExerciseSelection> = emptyList(),
    val words: List<ExerciseWordDetails> = emptyList(),

    val transactionId: String = "",
    val isActiveSubscribe: Boolean = false,
) {

    val currentExercise: Exercise
        get() = exercises[number].exercise

    val nextExercise: Exercise
        get() = exercises[number + 1].exercise

    val isLast: Boolean
        get() = number >= exercises.size - 1

    fun toNext(words: List<ExerciseWordDetails>): ExerciseBundle {
        val newNumber = number + 1
        return copy(
            number = newNumber,
            words = words
        )
    }

}