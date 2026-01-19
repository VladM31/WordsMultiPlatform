package  vm.words.ua.auth.ui.states

import vm.words.ua.auth.domain.models.TelegramAuthSession
import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.core.ui.states.ErrorableState


data class LoginState(
    val isAvailableBiometric: Boolean = false,
    val username: String = "",
    val password: String = "",
    val isGoogleSignInAvailable: Boolean = false,
    val telegramLoginSession: TelegramAuthSession? = null,
    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false
) : ErrorableState, EndetableState {

    val isPhone: Boolean
        get() = username.all { it.isDigit() }
}
