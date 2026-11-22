package vm.words.ua.auth.domain.models

/**
 * Начало привязки Telegram к аккаунту.
 *
 * @param code код, статус которого опрашивается до [vm.words.ua.auth.domain.models.enums.TelegramCodeStatus.LINKED]
 * @param botActivationRequired пользователю нужно открыть бота, нажать Start, отправить свой контакт
 * и подтвердить в сообщении
 */
data class TelegramLinkStart(
    val code: String,
    val botActivationRequired: Boolean
)
