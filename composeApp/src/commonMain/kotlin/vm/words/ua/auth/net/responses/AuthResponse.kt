package vm.words.ua.auth.net.responses
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val user: User? = null,
    val token: String? = null,
    val error: Error? = null
) {
    @Serializable
    data class User(
        val id: String,
        val phoneNumber: String,
        val email: String? = null,
        val firstName: String? = null,
        val lastName: String? = null
    )

    @Serializable
    data class Error(
        val message: String
    )

    val isSuccess: Boolean
        get() = error == null && user != null && token != null
}

