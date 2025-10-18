package vm.words.ua.auth.domain.models.data

internal data class AuthResult(
    val success: Boolean,
    val message: String? = null
)
