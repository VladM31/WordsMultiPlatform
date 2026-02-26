package vm.words.ua.auth.net.responses.telegram

import kotlinx.serialization.Serializable
import vm.words.ua.auth.domain.models.enums.TelegramMiniAppLoginStatus
import vm.words.ua.auth.net.responses.AuthResponse

@Serializable
data class TelegramMiniAppRespond(
    val status: TelegramMiniAppLoginStatus,
    val auth: AuthResponse?
)