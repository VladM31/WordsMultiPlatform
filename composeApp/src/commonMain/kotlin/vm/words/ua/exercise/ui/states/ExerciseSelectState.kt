package vm.words.ua.exercise.ui.states

import vm.words.ua.exercise.domain.models.data.ExerciseSelection
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise

data class ExerciseSelectState(
    val exercises: List<ExerciseSelection> = Exercise.entries.map {
        ExerciseSelection(it)
    },
    val selectedExercises: Map<Exercise, Int> = mapOf(),
    var number: Int = 0,
    val transactionId: String = "",
    val isConfirmed: Boolean = false,
    val isActiveSubscribe: Boolean = false,
    val isLoading: Boolean = false,

    val isInited: Boolean = false,
    val words: List<ExerciseWordDetails> = emptyList()
)

