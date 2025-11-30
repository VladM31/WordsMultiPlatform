package vm.words.ua.utils.validation.schemes

import vm.words.ua.utils.validation.actions.ValidAction
import vm.words.ua.utils.validation.models.ValidResult

open class ValidationScheme<T>(
    val name: String
) {
    private val container: MutableList<ValidAction<T>> = mutableListOf()

    fun add(action: ValidAction<T>): ValidationScheme<T> {
        container.add(action)
        return this
    }

    fun validateFirst(value: T): ValidResult {
        container.forEach {
            val result = it.validate(value)
            if (!result.isValid) {
                return result
            }
        }
        return ValidResult(true, null)
    }

    fun validateAll(value: Any?): List<ValidResult> {
        val validValue : T = value as T

        return container.map { it.validate(validValue) }
    }

    companion object {
        fun stringSchema(name: String): ValidationScheme<String> {
            return ValidationScheme(name)
        }
    }
}