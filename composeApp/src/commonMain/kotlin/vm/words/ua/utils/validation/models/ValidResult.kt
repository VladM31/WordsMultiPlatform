package vm.words.ua.utils.validation.models

data class ValidResult(
    val isValid: Boolean,
    val message: String? = null
){

    companion object{
        fun valid(): vm.words.ua.utils.validation.models.ValidResult =
            _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult(true)

        fun invalid(message: String): vm.words.ua.utils.validation.models.ValidResult =
            _root_ide_package_.vm.words.ua.utils.validation.models.ValidResult(false, message)
    }
}
