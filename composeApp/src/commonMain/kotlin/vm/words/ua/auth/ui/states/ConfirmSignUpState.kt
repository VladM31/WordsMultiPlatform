package  vm.words.ua.auth.ui.states

import vm.words.ua.core.ui.models.ErrorMessage

data class ConfirmSignUpState(
    val waitResult: Boolean = true,
    val error: ErrorMessage? = null,
    val phoneNumber: String = "",
    val password: String = "",
){

    fun isInited() = phoneNumber.isNotEmpty()
}
