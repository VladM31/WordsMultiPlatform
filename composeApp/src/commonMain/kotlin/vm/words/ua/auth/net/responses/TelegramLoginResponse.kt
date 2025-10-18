package vm.words.ua.auth.net.responses

import kotlinx.serialization.Serializable

@Serializable
data class TelegramLoginResponse(
    val success: Boolean = false,
    val message: String? = null,
    val authResponse: AuthResponse? = null
) {
    val isSuccess: Boolean
        get() = success && authResponse != null
}

