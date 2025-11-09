package vm.words.ua.auth.net.responses

import kotlinx.serialization.Serializable

@Serializable
data class TelegramLoginRespond(
    val success: Boolean,
    val user: AuthResponse.User? = null,
    val token: AuthResponse.Token? = null
) {
}
