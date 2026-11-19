package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

/**
 * Доказательство владения аккаунтом для чувствительных действий (смена пароля, удаление).
 *
 * - `GOOGLE`: нужен свежий Google `idToken` профиля, привязанного к аккаунту
 * - `TELEGRAM`: нужен `code` из `telegram-start-login`, подтверждённый кнопкой в боте (одноразовый)
 */
@Serializable
data class OwnershipProofRequest(
    val provider: String,
    val idToken: String? = null,
    val code: String? = null,
    /** Для `TELEGRAM` вместо `code`: `initData` Mini App */
    val initData: String? = null
)
