package vm.words.ua.auth.ui.validation

import vm.words.ua.auth.ui.states.LoginState
import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.validation.Validator
import vm.words.ua.validation.schemes.ValidationScheme
import vm.words.ua.validation.schemes.isPhoneNumber
import vm.words.ua.validation.schemes.length
import vm.words.ua.validation.schemes.notBlank

internal val loginValidator: (StateFlow<LoginState>) -> Validator<LoginState> = { state ->
    Validator(state).apply {
        add(
            LoginState::phoneNumber,
            ValidationScheme
                .stringSchema("Phone number")
                .isPhoneNumber()
        )
        add(
            LoginState::password,
            ValidationScheme
                .stringSchema("Password")
                .length(8, 60)
                .notBlank()
        )
    }
}