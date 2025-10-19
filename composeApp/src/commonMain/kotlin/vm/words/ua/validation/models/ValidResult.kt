package vm.words.ua.validation.models

data class ValidResult(
    val isValid: Boolean,
    val message: String? = null
){

    companion object{
        fun valid(): ValidResult = ValidResult(true)
        fun invalid(message: String): ValidResult = ValidResult(false, message)
    }
}
