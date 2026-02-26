package vm.words.ua.auth.net.requests.telegram

import kotlinx.serialization.Serializable

@Serializable
data class TelegramAuthLoginReq(
    val phoneNumber: String,
    val code: String
)