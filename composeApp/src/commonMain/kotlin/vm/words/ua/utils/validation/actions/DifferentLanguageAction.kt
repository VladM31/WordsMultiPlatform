package vm.words.ua.utils.validation.actions

class DifferentLanguageAction<T : vm.words.ua.utils.validation.models.DifferentLanguageable> :
    vm.words.ua.utils.validation.actions.ValidAction<T> {
    override fun validate(value: T): vm.words.ua.utils.validation.models.ValidResult {
        if (value.getLanguage() == value.getSecondLanguage()) {
            return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid("Languages should be different")
        }
        return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
    }
}