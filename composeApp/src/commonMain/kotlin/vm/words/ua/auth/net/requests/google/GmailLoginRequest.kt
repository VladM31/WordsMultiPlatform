package vm.words.ua.auth.net.requests.google

import kotlinx.serialization.Serializable

@Serializable
data class GmailLoginRequest(
    val email: String,
    val password: String
)