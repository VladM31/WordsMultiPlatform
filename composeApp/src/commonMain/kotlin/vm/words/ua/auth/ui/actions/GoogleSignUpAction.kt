package vm.words.ua.auth.ui.actions

import vm.words.ua.core.domain.models.enums.Currency

sealed interface GoogleSignUpAction {
    data object Submit : GoogleSignUpAction

    data class SetAgreed(val value: Boolean) : GoogleSignUpAction
    data class SetPassword(val value: String) : GoogleSignUpAction
    data class SetFirstName(val value: String) : GoogleSignUpAction
    data class SetLastName(val value: String) : GoogleSignUpAction
    data class SetCurrency(val value: Currency) : GoogleSignUpAction
}