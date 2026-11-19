package vm.words.ua.auth.domain.models.enums

/**
 * Результат `POST /auth/telegram/mini-app/login` (ответ всегда 200, смотреть поле `status`).
 *
 * - [SUCCESS]: вход выполнен, токен и пользователь в `auth`
 * - [NOT_FOUND_IN_BOT]: бот не знает телефон, нужно открыть бота и отправить контакт
 * - [ERROR]: подпись неверна, данные устарели или бот недоступен
 */
enum class TelegramMiniAppLoginStatus {
    NOT_FOUND_IN_BOT, ERROR, SUCCESS
}
