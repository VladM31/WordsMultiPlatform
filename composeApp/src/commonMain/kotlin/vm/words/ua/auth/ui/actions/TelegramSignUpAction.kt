package vm.words.ua.auth.ui.actions

import vm.words.ua.core.domain.models.enums.Currency

sealed interface TelegramSignUpAction {
    data object Submit : TelegramSignUpAction

    data class SetAgreed(val value: Boolean) : TelegramSignUpAction

    data class SetPhoneNumber(val value: String) : TelegramSignUpAction
    data class SetPassword(val value: String) : TelegramSignUpAction
    data class SetFirstName(val value: String) : TelegramSignUpAction
    data class SetLastName(val value: String) : TelegramSignUpAction
    data class SetCurrency(val value: Currency) : TelegramSignUpAction
}