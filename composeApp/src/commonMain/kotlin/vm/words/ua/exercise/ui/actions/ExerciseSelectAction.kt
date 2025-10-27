package vm.words.ua.exercise.ui.actions

import vm.words.ua.exercise.domain.models.enums.Exercise

sealed class ExerciseSelectAction {
    data class AddExercise(val exercise: Exercise) : ExerciseSelectAction()
    data class RemoveExercise(val exercise: Exercise) : ExerciseSelectAction()
    object ConfirmSelection : ExerciseSelectAction()
}

