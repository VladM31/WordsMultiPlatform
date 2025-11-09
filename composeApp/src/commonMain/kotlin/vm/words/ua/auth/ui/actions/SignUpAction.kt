package vm.words.ua.auth.ui.actions

import vm.words.ua.core.domain.models.enums.Currency

sealed interface SignUpAction {
    data object Submit : SignUpAction

    data class SetAgreed(val value: Boolean) : SignUpAction

    data class SetPhoneNumber(val value: String) : SignUpAction
    data class SetPassword(val value: String) : SignUpAction
    data class SetFirstName(val value: String) : SignUpAction
    data class SetLastName(val value: String) : SignUpAction
    data class SetCurrency(val value: Currency) : SignUpAction
    data class SetEmail(val value: String) : SignUpAction
}