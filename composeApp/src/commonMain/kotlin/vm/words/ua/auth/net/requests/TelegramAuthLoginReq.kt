package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class TelegramAuthLoginReq(
    val phoneNumber: String,
    val code: String
)

