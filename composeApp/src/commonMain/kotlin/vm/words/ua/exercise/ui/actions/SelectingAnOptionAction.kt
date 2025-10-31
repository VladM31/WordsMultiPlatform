package vm.words.ua.exercise.ui.actions

import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails

import vm.words.ua.exercise.domain.models.enums.Exercise

interface SelectingAnOptionAction {
    data class Init(
        val words: List<ExerciseWordDetails>,
        val exerciseType : Exercise,
        val transactionId: String,
        val isActiveSubscribe: Boolean,
        val isSoundBeforeAnswer: Boolean,
        val isSoundAfterAnswer: Boolean
    ) : SelectingAnOptionAction
    data class ChooseWord(val word: ExerciseWordDetails) : SelectingAnOptionAction
    data object Next : SelectingAnOptionAction
}