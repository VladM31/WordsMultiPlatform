package vm.words.ua.auth.domain.models.enums

/**
 * Состояние кода подтверждения, `GET /auth/telegram-status`.
 *
 * - [PENDING]: кнопка в боте ещё не нажата
 * - [CONFIRMED]: нажато Yes, код можно использовать
 * - [LINKED]: привязка Telegram к аккаунту выполнена (`POST /user/identities/telegram`)
 * - [CONFLICT]: нажато Yes, но номер или профиль Telegram принадлежит другому аккаунту, ничего не привязано
 * - [REFUSED]: нажато No
 * - [USED]: код уже использован
 * - [EXPIRED]: код неизвестен или старше 15 минут
 */
enum class TelegramCodeStatus {
    PENDING, CONFIRMED, LINKED, CONFLICT, REFUSED, USED, EXPIRED
}
