package vm.words.ua.validation.actions

import vm.words.ua.validation.models.ValidResult

internal data class NotBlankAction(
    val canBeEmpty: Boolean
) : ValidAction<String> {
    override fun validate(value: String): ValidResult {
        if (canBeEmpty && value.isEmpty()) {
            return ValidResult.valid()
        }

        if (value.isBlank()) {
            return ValidResult.invalid("Value should not be blank")
        }
        return ValidResult.valid()
    }
}