package vm.words.ua.words.ui.actions

import vm.words.ua.words.domain.models.filters.UserWordFilter

sealed interface UserWordsAction {
    data class SelectWord(val wordId: String, val position: Int) : UserWordsAction
    data class UnSelectWord(val wordId: String) : UserWordsAction
    data class ChangeFilter(val filter: UserWordFilter) : UserWordsAction
    data object Clear : UserWordsAction
    data class PinWords(val playListId: String) : UserWordsAction
    data object ReFetch : UserWordsAction
    data object LoadMore : UserWordsAction
}