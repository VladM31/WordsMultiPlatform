package vm.words.ua.utils.validation

import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.set
import kotlin.text.isBlank
import kotlin.text.isNotBlank
import kotlin.text.orEmpty

typealias SupplierMapper  <T,U>   = (T) -> U

class Validator<T>(
    private val state: StateFlow<T>
) {
    private val container: MutableMap<vm.words.ua.utils.validation.SupplierMapper<T, *>, vm.words.ua.utils.validation.schemes.ValidationScheme<*>> =
        mutableMapOf()


    fun <U> add(
        mapper: vm.words.ua.utils.validation.SupplierMapper<T, U>,
        scheme: vm.words.ua.utils.validation.schemes.ValidationScheme<U>
    ) {
        container[mapper] set scheme
    }

    fun validate(prefix: String = ""): String {
        return container.map { (mapper, scheme) ->
            val value = mapper(state.value)

            val message = scheme.validateAll(value)
                .filter { it.isValid.not() }
                .joinToString("\n",transform = { prefix + it.message.orEmpty()})

            if (message.isBlank()) {
                return@map ""
            }

            scheme.name + ":\n" + message
        }.filter { it.isNotBlank() }.joinToString("\n")
    }

    fun <T> getSchema(name: String): vm.words.ua.utils.validation.schemes.ValidationScheme<T>? {
        return container.values.firstOrNull { it.name == name } as? vm.words.ua.utils.validation.schemes.ValidationScheme<T>
    }
}