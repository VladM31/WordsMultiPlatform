package vm.words.ua.validation.actions

import vm.words.ua.validation.models.DifferentLanguageable
import vm.words.ua.validation.models.ValidResult

class DifferentLanguageAction<T: DifferentLanguageable> : ValidAction<T> {
    override fun validate(value: T): ValidResult {
        if (value.getLanguage() == value.getSecondLanguage()) {
            return ValidResult.invalid("Languages should be different")
        }
        return ValidResult.valid()
    }
}