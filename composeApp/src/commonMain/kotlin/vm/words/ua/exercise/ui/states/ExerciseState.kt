package vm.words.ua.exercise.ui.states

import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise

interface ExerciseState : EndetableState {
    val transactionId: String
    val exercise: Exercise
    val words: List<ExerciseWordDetails>
}