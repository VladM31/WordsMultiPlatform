package vm.words.ua.utils.validation.actions

internal data class StringLengthAction(
    val min: Int? = null,
    val max: Int? = null
) : vm.words.ua.utils.validation.actions.ValidAction<String> {
    override fun validate(value: String): vm.words.ua.utils.validation.models.ValidResult {
        if (min != null && value.length < min) {
            return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid("Length should be at least $min")
        }
        if (max != null && value.length > max) {
            return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid("Length should be at most $max")
        }
        return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
    }
}

