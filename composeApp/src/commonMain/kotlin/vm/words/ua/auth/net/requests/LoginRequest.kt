package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

/**
 * `POST /auth/login`. [login] это телефон в любом формате или email
 * (если есть `@`, бэк ищет по email).
 */
@Serializable
data class LoginRequest(
    val login: String,
    val password: String
)
