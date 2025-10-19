package vm.words.ua.validation.actions

import vm.words.ua.validation.models.ValidResult

internal object NotEmptyAction : ValidAction<String> {
    override fun validate(value: String): ValidResult {
        if (value.isEmpty()) {
            return ValidResult.invalid("Value should not be empty")
        }
        return ValidResult.valid()
    }
}