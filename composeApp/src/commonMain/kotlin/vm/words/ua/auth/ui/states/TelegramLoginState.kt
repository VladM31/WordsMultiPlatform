package  vm.words.ua.auth.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.core.ui.states.ErrorableState

internal data class TelegramLoginState(
    val code: String = "",
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false

): ErrorableState, EndetableState
