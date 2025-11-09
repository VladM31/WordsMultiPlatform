package vm.words.ua.words.ui.actions

import vm.words.ua.words.domain.models.filters.UserWordFilter

sealed interface UserWordsAction {
    data class SelectWord(val wordId: String) : UserWordsAction
    data class UpdateFilter(val filter: UserWordFilter) : UserWordsAction
    data object Clear : UserWordsAction
    data class PinWords(val playListId: String) : UserWordsAction
    data object ReFetch : UserWordsAction
    data object LoadMore : UserWordsAction
}