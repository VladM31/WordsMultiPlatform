package vm.words.ua.words.ui.validations

import vm.words.ua.words.ui.states.DefaultAddWordState


private object DifLanguageAction : vm.words.ua.utils.validation.actions.ValidAction<DefaultAddWordState> {
    override fun validate(value: DefaultAddWordState): vm.words.ua.utils.validation.models.ValidResult {
        return if (value.language != value.translationLanguage) {
            _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.valid()
        } else {
            _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.invalid("Languages must be different")
        }
    }
}

fun vm.words.ua.utils.validation.schemes.ValidationScheme<DefaultAddWordState>.difLanguage(): vm.words.ua.utils.validation.schemes.ValidationScheme<DefaultAddWordState> {
    return add(DifLanguageAction)
}