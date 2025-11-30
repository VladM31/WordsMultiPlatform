package vm.words.ua.utils.validation.schemes

open class ValidationScheme<T>(
    val name: String
) {
    private val container: MutableList<vm.words.ua.utils.validation.actions.ValidAction<T>> = mutableListOf()

    fun add(action: vm.words.ua.utils.validation.actions.ValidAction<T>): vm.words.ua.utils.validation.schemes.ValidationScheme<T> {
        container.add(action)
        return this
    }

    fun validateFirst(value: T): vm.words.ua.utils.validation.models.ValidResult {
        container.forEach {
            val result = it.validate(value)
            if (!result.isValid) {
                return result
            }
        }
        return _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult(true, null)
    }

    fun validateAll(value: Any?): List<vm.words.ua.utils.validation.models.ValidResult> {
        val validValue : T = value as T

        return container.map { it.validate(validValue) }
    }

    companion object {
        fun stringSchema(name: String): vm.words.ua.utils.validation.schemes.ValidationScheme<String> {
            return _root_ide_package_.vm.words.ua.utils.validation.schemes.ValidationScheme(name)
        }
    }
}