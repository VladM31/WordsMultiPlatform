package vm.words.ua.words.ui.validations

import vm.words.ua.validation.actions.ValidAction
import vm.words.ua.validation.models.ValidResult
import vm.words.ua.validation.schemes.ValidationScheme
import vm.words.ua.words.ui.states.DefaultAddWordState


private object DifLanguageAction : ValidAction<DefaultAddWordState> {
    override fun validate(value: DefaultAddWordState): ValidResult {
        return if (value.language != value.translationLanguage) {
            ValidResult.valid()
        } else {
            ValidResult.invalid("Languages must be different")
        }
    }
}

fun ValidationScheme<DefaultAddWordState>.difLanguage(): ValidationScheme<DefaultAddWordState> {
    return add(DifLanguageAction)
}