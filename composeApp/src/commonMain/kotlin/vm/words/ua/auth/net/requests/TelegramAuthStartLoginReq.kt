package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class TelegramAuthStartLoginReq(
    val phoneNumber: String
)

