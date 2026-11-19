package vm.words.ua.auth.domain.models.enums

/**
 * Состояние кода подтверждения, `GET /auth/telegram-status`.
 *
 * - [PENDING]: кнопка в боте ещё не нажата
 * - [CONFIRMED]: нажато Yes, код можно использовать
 * - [REFUSED]: нажато No
 * - [USED]: код уже использован
 * - [EXPIRED]: код неизвестен или старше 15 минут
 */
enum class TelegramCodeStatus {
    PENDING, CONFIRMED, REFUSED, USED, EXPIRED
}
