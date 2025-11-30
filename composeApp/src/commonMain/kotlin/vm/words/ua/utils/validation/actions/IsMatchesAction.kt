package vm.words.ua.utils.validation.actions

internal data class IsMatchesAction(
    private val regex: Regex,
    private val message: String = "Value should match the pattern"
) : vm.words.ua.utils.validation.actions.ValidAction<String> {
    override fun validate(value: String): vm.words.ua.utils.validation.models.ValidResult {
        if (!regex.matches(value)) {
            return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.invalid(message)
        }
        return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult.Companion.valid()
    }
}
