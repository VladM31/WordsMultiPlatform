package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val phoneNumber: String,
    val password: String,
    val email: String?,
    val firstName: String,
    val lastName: String,
    val currency: String
)

