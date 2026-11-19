package vm.words.ua.auth.net.requests.telegram

import kotlinx.serialization.Serializable

@Serializable
data class TelegramAuthStartLoginReq(
    val phoneNumber: String,
    val deviceName: String?,
    /**
     * Что подтверждается, от этого зависит текст сообщения бота: `SET_PASSWORD` или `DELETE_ACCOUNT`.
     * `null` значит вход или регистрация.
     */
    val purpose: String? = null
)