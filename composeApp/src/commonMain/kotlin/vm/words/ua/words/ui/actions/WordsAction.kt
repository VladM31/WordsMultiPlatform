package vm.words.ua.words.ui.actions

import vm.words.ua.words.domain.models.filters.WordFilter

sealed interface WordsAction {
    data class SelectWord(val wordId: String) : WordsAction
    data class RemoveWord(val wordId: String) : WordsAction
    data object Clear : WordsAction
    data class UpdateFilter(val filter: WordFilter) : WordsAction
    data object LoadMore : WordsAction
}