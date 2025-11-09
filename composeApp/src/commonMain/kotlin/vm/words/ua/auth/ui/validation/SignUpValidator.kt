package vm.words.ua.auth.ui.validation

import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.auth.ui.states.SignUpState
import vm.words.ua.validation.Validator
import vm.words.ua.validation.schemes.ValidationScheme
import vm.words.ua.validation.schemes.email
import vm.words.ua.validation.schemes.isPhoneNumber
import vm.words.ua.validation.schemes.length
import vm.words.ua.validation.schemes.notBlank

val signUpValidator: (StateFlow<SignUpState>) -> Validator<SignUpState> = { state ->
    Validator(state).apply {
        add(
            SignUpState::phoneNumber,
            ValidationScheme.stringSchema("Phone number")
                .isPhoneNumber()
        )
        add(
            SignUpState::password,
            ValidationScheme.stringSchema("Password")
                .length(8, 60)
                .notBlank()
        )

        add(
            SignUpState::firstName,
            ValidationScheme.stringSchema("First name")
                .length(2, 60)
                .notBlank()
        )

        add(
            SignUpState::lastName,
            ValidationScheme.stringSchema("Last name")
                .length(2, 60)
                .notBlank()
        )

        add(
            { it.email.orEmpty() },
            ValidationScheme.stringSchema("Email")
                .email(isRequired = false)
                .notBlank(canBeEmpty = true)
        )


    }
}