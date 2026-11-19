package vm.words.ua.auth.domain.models

/**
 * Для чего подтверждается владение аккаунтом через Telegram.
 * Бот пишет в сообщении, что именно подтверждается (пароль или удаление), а не "вход".
 */
enum class OwnershipPurpose(val value: String) {
    SET_PASSWORD("SET_PASSWORD"),
    DELETE_ACCOUNT("DELETE_ACCOUNT")
}
