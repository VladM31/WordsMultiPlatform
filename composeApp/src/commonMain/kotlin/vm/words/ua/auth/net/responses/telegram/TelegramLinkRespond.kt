package vm.words.ua.auth.net.responses.telegram

import kotlinx.serialization.Serializable

/**
 * Ответ `POST /user/identities/telegram`.
 *
 * @param code код для опроса `GET /auth/telegram-status`
 * @param botActivationRequired бот ещё не знает этот номер: сообщение ждёт, пока пользователь
 * нажмёт Start в боте и отправит свой контакт
 */
@Serializable
data class TelegramLinkRespond(
    val code: String,
    val botActivationRequired: Boolean
)
