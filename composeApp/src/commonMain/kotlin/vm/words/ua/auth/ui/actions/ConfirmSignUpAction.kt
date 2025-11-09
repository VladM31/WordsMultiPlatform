package vm.words.ua.auth.ui.actions

sealed interface ConfirmSignUpAction {
    data class Init(val phoneNumber: String, val password: String) : ConfirmSignUpAction
}