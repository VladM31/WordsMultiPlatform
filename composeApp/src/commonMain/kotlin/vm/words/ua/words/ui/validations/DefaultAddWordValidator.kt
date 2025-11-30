package vm.words.ua.words.ui.validations

import kotlinx.coroutines.flow.StateFlow
import vm.words.ua.utils.validation.schemes.length
import vm.words.ua.utils.validation.schemes.notBlank
import vm.words.ua.words.ui.states.DefaultAddWordState

val defaultAddWordValidator: (StateFlow<DefaultAddWordState>) -> vm.words.ua.utils.validation.Validator<DefaultAddWordState> =
    { state ->
        _root_ide_package_.vm.words.ua.utils.validation.Validator(state).apply {


        add(
            DefaultAddWordState::originalWord,
            _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Word")
                .notBlank()
                .length(min = 2, max = 255)
        )

        add(
            DefaultAddWordState::translation,
            _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Translation")

        )

        add(
            DefaultAddWordState::category,
            _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Category")
                .length(min = 0, max = 255)
                .notBlank(canBeEmpty = true)
        )

        add(
            DefaultAddWordState::description,
            _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme.stringSchema("Description")
                .length(min = 0, max = 1000)
                .notBlank(canBeEmpty = true)
        )

        add(
            { it },
            _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme<DefaultAddWordState>("Language")
                .difLanguage()
        )
    }
}