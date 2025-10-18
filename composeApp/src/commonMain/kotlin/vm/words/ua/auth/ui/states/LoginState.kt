package  vm.words.ua.auth.ui.states

import vm.words.ua.core.ui.models.ErrorMessage
import vm.words.ua.core.ui.states.EndetableState
import vm.words.ua.core.ui.states.ErrorableState


data class LoginState(
    val isNotExpired: Boolean = false,
    val isAvailableBiometric: Boolean = false,
    val phoneNumber: String = "",
    val password: String = "",
    override val errorMessage: ErrorMessage? = null,
    override val isEnd: Boolean = false
) : ErrorableState, EndetableState
