package vm.words.ua.words.ui.validations

import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.validation.Validator
import vm.words.ua.validation.schemes.ValidationScheme
import vm.words.ua.validation.schemes.length
import vm.words.ua.validation.schemes.notBlank
import vm.words.ua.words.ui.states.DefaultAddWordState

val defaultAddWordValidator: (StateFlow<DefaultAddWordState>) -> Validator<DefaultAddWordState> = { state ->
    Validator(state).apply {


        add(
            DefaultAddWordState::originalWord,
            ValidationScheme.stringSchema("Word")
                .notBlank()
                .length(min = 2, max = 255)
        )

        add(
            DefaultAddWordState::translation,
            ValidationScheme.stringSchema("Translation")

        )

        add(
            DefaultAddWordState::category,
            ValidationScheme.stringSchema("Category")
                .length(min = 0, max = 255)
                .notBlank(canBeEmpty = true)
        )

        add(
            DefaultAddWordState::description,
            ValidationScheme.stringSchema("Description")
                .length(min = 0, max = 1000)
                .notBlank(canBeEmpty = true)
        )

        add(
            { it }, ValidationScheme<DefaultAddWordState>("Language")
                .difLanguage()
        )
    }
}