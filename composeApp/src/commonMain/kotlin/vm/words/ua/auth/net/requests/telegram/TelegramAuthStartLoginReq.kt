package vm.words.ua.auth.net.requests.telegram

import kotlinx.serialization.Serializable

@Serializable
data class TelegramAuthStartLoginReq(
    val phoneNumber: String,
    val deviceName: String?
)