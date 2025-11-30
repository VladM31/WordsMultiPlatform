package vm.words.ua.utils.validation.actions

import vm.words.ua.utils.validation.models.DifferentLanguageable
import vm.words.ua.utils.validation.models.ValidResult

class DifferentLanguageAction<T : DifferentLanguageable> :
    ValidAction<T> {
    override fun validate(value: T): ValidResult {
        if (value.getLanguage() == value.getSecondLanguage()) {
            return ValidResult.invalid("Languages should be different")
        }
        return ValidResult.valid()
    }
}