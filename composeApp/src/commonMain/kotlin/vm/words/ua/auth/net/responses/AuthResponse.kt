package vm.words.ua.auth.net.responses
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user : User? = null,
    val token : Token? = null,
    val error: Error? = null,
    val tempToken: String? = null
) {
    @Serializable
    data class Token(
        val value : String,
        val expirationTime : Long
    )

    @Serializable
    data class User(
        val id: String,
        val phoneNumber: String?,
        val firstName: String,
        val lastName: String,
        val currency: String,
        val email: String?,
        val role: String
    )
    @Serializable
    data class Error(
        val message: String
    )

    fun hasError() = error != null
}

