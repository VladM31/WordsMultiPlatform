package vm.words.ua.utils.validation.actions

internal data class NotBlankAction(
    val canBeEmpty: Boolean
) : vm.words.ua.utils.validation.actions.ValidAction<String> {
    override fun validate(value: String): vm.words.ua.utils.validation.models.ValidResult {
        if (canBeEmpty && value.isEmpty()) {
            return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
        }

        if (value.isBlank()) {
            return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid("Value should not be blank")
        }
        return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
    }
}