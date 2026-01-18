package vm.words.ua.auth.ui.validation

import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.auth.ui.states.SignUpState
import vm.words.ua.utils.validation.schemes.isPhoneNumber
import vm.words.ua.utils.validation.schemes.length
import vm.words.ua.utils.validation.schemes.notBlank

val signUpValidator: (StateFlow<SignUpState>) -> vm.words.ua.utils.validation.Validator<SignUpState> = { state ->
    vm.words.ua.utils.validation.Validator(state).apply {
        add(
            SignUpState::phoneNumber,
            vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Phone number")
                .isPhoneNumber()
        )
        add(
            SignUpState::password,
            vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Password")
                .length(8, 60)
                .notBlank()
        )

        add(
            SignUpState::firstName,
            vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("First name")
                .length(2, 60)
                .notBlank()
        )

        add(
            SignUpState::lastName,
            vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Last name")
                .length(2, 60)
                .notBlank()
        )

    }
}