package vm.words.ua.words.ui.states

import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.filters.UserWordFilter

data class UserWordsState(
    val selectedWords: Map<String, Int> = emptyMap(),
    val filter: UserWordFilter = UserWordFilter(),
    val userWords: List<UserWord> = emptyList(),
    val count: Long = 0,
    val page: Int = 0,
    val isLoading: Boolean = false,
    val selectedPlayListId: String? = null,
    val openPlayList: OpenPlayList? = null,
) {

    data class OpenPlayList(
        val id: String
    )
}
