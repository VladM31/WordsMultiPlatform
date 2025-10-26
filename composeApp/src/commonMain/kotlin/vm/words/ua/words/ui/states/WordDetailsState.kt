package vm.words.ua.words.ui.states

import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word

data class WordDetailsState(
    val userWord: UserWord? = null,
    val word: Word? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDeleted: Boolean = false,
    val isPlayingSound: Boolean = false
)

