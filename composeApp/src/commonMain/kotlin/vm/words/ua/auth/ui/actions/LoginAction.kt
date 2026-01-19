package vm.words.ua.auth.ui.actions

sealed interface LoginAction{
    data object Submit: LoginAction
    data class SetUsername(val value: String) : LoginAction
    data class SetPassword(val value: String): LoginAction

    data object GoogleSignIn : LoginAction
}