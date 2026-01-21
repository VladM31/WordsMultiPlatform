package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequest(
    val password: String,
    val reason: String?
)