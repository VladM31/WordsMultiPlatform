package vm.words.ua.words.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.filters.UserWordFilter

data class UserWordsState(
    val selectedWords: Set<String> = emptySet(),
    val filter: UserWordFilter = UserWordFilter(),
    val userWords: List<UserWord> = emptyList(),
    val count: Long = 0,
    val page: Int = 0,
    val hasMore: Boolean = true,
    val isLoading: Boolean = false,
    val selectedPlayListId: String? = null,
    val openPlayList: OpenPlayList? = null,
    override val errorMessage: ErrorMessage? = null,
) : ErrorableState {

    data class OpenPlayList(
        val id: String
    )
}
