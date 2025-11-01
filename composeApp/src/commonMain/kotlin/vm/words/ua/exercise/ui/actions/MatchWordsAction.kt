package vm.words.ua.exercise.ui.actions

import vm.words.ua.exercise.domain.models.data.ExerciseWordDetails

sealed interface MatchWordsAction {
    data class Init(
        val words: List<ExerciseWordDetails>,
        val transactionId: String
    ) : MatchWordsAction
    data class Click(val isOriginal: Boolean, val wordId: String, val index: Int) :
        MatchWordsAction
}