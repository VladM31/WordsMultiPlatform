package vm.words.ua.auth.net.requests.telegram

import kotlinx.serialization.Serializable

/** `POST /user/identities/telegram`: номер, который привязывается к текущему аккаунту. */
@Serializable
data class TelegramLinkRequest(
    val phoneNumber: String,
    val deviceName: String? = null
)
