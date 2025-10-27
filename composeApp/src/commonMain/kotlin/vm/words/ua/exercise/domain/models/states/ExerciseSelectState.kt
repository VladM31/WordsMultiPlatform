package vm.words.ua.exercise.domain.models.states

import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.exercise.domain.models.data.ExerciseSelection

data class ExerciseSelectState(
    val exercises: List<ExerciseSelection> = Exercise.entries.map {
        ExerciseSelection(it)
    },
    val selectedExercises: Map<Exercise, Int> = mapOf(),
    var number: Int = 0,
    val isConfirmed: Boolean = false
)

