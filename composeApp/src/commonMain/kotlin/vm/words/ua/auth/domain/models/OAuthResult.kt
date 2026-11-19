package vm.words.ua.auth.domain.models


data class OAuthResult(
    val success: Boolean,
    val message: String? = null
)
