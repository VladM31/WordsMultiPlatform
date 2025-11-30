package vm.words.ua.auth.ui.validation

import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.auth.ui.states.LoginState
import vm.words.ua.utils.validation.schemes.isPhoneNumber
import vm.words.ua.utils.validation.schemes.length
import vm.words.ua.utils.validation.schemes.notBlank

internal val loginValidator: (StateFlow<LoginState>) -> vm.words.ua.utils.validation.Validator<LoginState> = { state ->
    vm.words.ua.utils.validation.Validator(state).apply {
        add(
            LoginState::phoneNumber,
            vm.words.ua.utils.validation.schemes.ValidationScheme
                .stringSchema("Phone number")
                .isPhoneNumber()
        )
        add(
            LoginState::password,
            vm.words.ua.utils.validation.schemes.ValidationScheme
                .stringSchema("Password")
                .length(8, 60)
                .notBlank()
        )
    }
}