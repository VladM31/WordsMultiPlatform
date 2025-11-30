package vm.words.ua.utils.validation.actions

internal object NotEmptyAction : vm.words.ua.utils.validation.actions.ValidAction<String> {
    override fun validate(value: String): vm.words.ua.utils.validation.models.ValidResult {
        if (value.isEmpty()) {
            return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid("Value should not be empty")
        }
        return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
    }
}