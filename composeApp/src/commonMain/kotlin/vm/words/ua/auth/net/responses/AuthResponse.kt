package vm.words.ua.auth.net.responses
import kotlinx.serialization.Serializable

/**
 * Ответ входа бэкенда (`AuthRespond`): токен и пользователь.
 * Бэк не отдаёт `currency`, `error` и `tempToken`, ошибки приходят статусом и текстом.
 */
@Serializable
data class AuthResponse(
    val user : User? = null,
    val token: Token? = null
) {
    @Serializable
    data class Token(
        val value : String,
        val expirationTime : Long
    )

    @Serializable
    data class User(
        val id: String,
        val phoneNumber: String? = null,
        val firstName: String,
        val lastName: String,
        val email: String? = null,
        val role: String,
        /** Задан ли пароль. `null`, если бэк старой версии не отдаёт признак */
        val hasPassword: Boolean? = null
    )
}

