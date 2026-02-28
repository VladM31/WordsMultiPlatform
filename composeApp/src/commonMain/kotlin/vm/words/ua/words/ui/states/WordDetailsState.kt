package vm.words.ua.words.ui.states

import vm.words.ua.core.domain.models.ByteContent
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.ErrorableState
import vm.words.ua.words.domain.models.UserWord
import vm.words.ua.words.domain.models.Word

data class WordDetailsState(
    val userWord: UserWord? = null,
    val word: Word? = null,
    val image: ByteContent? = null,
    val sound: ByteContent? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val isInited: Boolean = false,
    val isReloaded: Boolean = false,
    val isPlayingSound: Boolean = false,
    val isActiveSubscribe: Boolean = false,
    override val errorMessage: ErrorMessage? = null
) : ErrorableState

