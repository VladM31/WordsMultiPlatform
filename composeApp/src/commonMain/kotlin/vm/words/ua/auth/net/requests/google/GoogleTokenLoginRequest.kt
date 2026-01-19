package vm.words.ua.auth.net.requests.google

import kotlinx.serialization.Serializable

@Serializable
data class GoogleTokenLoginRequest(
    val idToken: String
)
