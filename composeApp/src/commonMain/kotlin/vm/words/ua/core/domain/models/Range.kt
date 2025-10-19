package vm.words.ua.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Range<T>(
    val to: T? = null,
    val from: T? = null
){

    companion object{
        fun <T> of(value: T) : Range<T> {
            return Range(value, value)
        }
    }
}
