package vm.words.ua.auth.net.requests.google

import kotlinx.serialization.Serializable

/**
 * `POST /auth/google`: единый вход и регистрация через Google.
 * [idToken] это Google `id_token` (на Desktop и Web его выдаёт `/auth/google/exchange` или Firebase).
 */
@Serializable
data class GoogleSignInRequest(
    val idToken: String
)
