package vm.words.ua.exercise.ui.actions

import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails
import vm.words.ua.exercise.domain.models.enums.Exercise

interface LettersMatchAction {

    data class Init(
        val words: List<ExerciseWordDetails>,
        val isActiveSubscribe: Boolean,
        val transactionId: String,
        val exerciseType: Exercise
    ) : LettersMatchAction

    data class ClickOnLetter(val letter: Char, val id: String) : LettersMatchAction

    data object Next : LettersMatchAction

    data object PlusOneLetter : LettersMatchAction
}