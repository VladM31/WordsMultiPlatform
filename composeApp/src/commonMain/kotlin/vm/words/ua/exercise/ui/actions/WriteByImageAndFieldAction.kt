package vm.words.ua.exercise.ui.actions

import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise

sealed interface WriteByImageAndFieldAction {
    data class Init(
        val words: List<ExerciseWordDetails>,
        val transactionId: String,
        val exerciseType: Exercise,
        val isActiveSubscribe: Boolean = false
    ) : WriteByImageAndFieldAction

    data class UpdateText(val text: String) : WriteByImageAndFieldAction
    data object Confirm : WriteByImageAndFieldAction
    data object NextWord : WriteByImageAndFieldAction
    data object AddLetter : WriteByImageAndFieldAction
}