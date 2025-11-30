package vm.words.ua.utils.validation.actions

internal data class EqualsAction<T>(
    val values: List<T>,
    val isEqual: Boolean = true
) : vm.words.ua.utils.validation.actions.ValidAction<T> {
    override fun validate(value: T): vm.words.ua.utils.validation.models.ValidResult {
        if (values.contains(value) == isEqual) {
            return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
        }
        return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid("Value should be one of $values")
    }
}