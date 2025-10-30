package vm.words.ua.exercise.ui.actions

import vm.words.ua.exercise.domain.models.enums.Exercise
import vm.words.ua.words.domain.models.UserWord

sealed class ExerciseSelectAction {
    data class Init(
        val playListId: String?,
        val words: List<UserWord>
    ) : ExerciseSelectAction()

    data class AddExercise(val exercise: Exercise) : ExerciseSelectAction()
    data class RemoveExercise(val exercise: Exercise) : ExerciseSelectAction()
    object ConfirmSelection : ExerciseSelectAction()
}

