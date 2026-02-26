package vm.words.ua.auth.net.requests.telegram

import kotlinx.serialization.Serializable

@Serializable
data class TelegramMiniAppLoginRequest(
    val initData: String
)
