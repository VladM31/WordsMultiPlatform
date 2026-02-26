package vm.words.ua.auth.net.responses.telegram

import vm.words.ua.auth.domain.models.enums.TelegramMiniAppLoginStatus
import vm.words.ua.auth.net.responses.AuthResponse

data class TelegramMiniAppRespond(
    val status: TelegramMiniAppLoginStatus,
    val auth: AuthResponse?
)