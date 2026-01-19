package vm.words.ua.auth.domain.models

data class AuthResult(
    val success: Boolean,
    val message: String? = null
)
