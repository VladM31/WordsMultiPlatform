package vm.words.ua.auth.ui.validation

import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.auth.ui.states.GoogleSignUpState
import vm.words.ua.auth.ui.states.TelegramSignUpState
import vm.words.ua.utils.validation.schemes.isPhoneNumber
import vm.words.ua.utils.validation.schemes.length
import vm.words.ua.utils.validation.schemes.notBlank

val telegramSignUpValidator: (StateFlow<TelegramSignUpState>) -> vm.words.ua.utils.validation.Validator<TelegramSignUpState> =
    { state ->
    vm.words.ua.utils.validation.Validator(state).apply {
        add(
            TelegramSignUpState::phoneNumber,
            vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Phone number")
                .isPhoneNumber()
        )
        add(
            TelegramSignUpState::password,
            vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Password")
                .length(8, 60)
                .notBlank()
        )

        add(
            TelegramSignUpState::firstName,
            vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("First name")
                .length(2, 60)
                .notBlank()
        )

        add(
            TelegramSignUpState::lastName,
            vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Last name")
                .length(2, 60)
                .notBlank()
        )

    }
}

val googleSignUpValidator: (StateFlow<GoogleSignUpState>) -> vm.words.ua.utils.validation.Validator<GoogleSignUpState> =
    { state ->
        vm.words.ua.utils.validation.Validator(state).apply {
            add(
                GoogleSignUpState::password,
                vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Password")
                    .length(8, 60)
                    .notBlank()
            )
            add(
                GoogleSignUpState::firstName,
                vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("First name")
                    .length(2, 60)
                    .notBlank()
            )
            add(
                GoogleSignUpState::lastName,
                vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Last name")
                    .length(2, 60)
                    .notBlank()
            )

        }
    }