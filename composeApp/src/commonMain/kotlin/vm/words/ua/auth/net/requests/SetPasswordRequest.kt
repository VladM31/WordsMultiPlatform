package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

/**
 * `POST /user/password`.
 * Смена пароля: [oldPassword] или [proof]. Первый пароль или забытый: только [proof].
 */
@Serializable
data class SetPasswordRequest(
    val newPassword: String,
    val oldPassword: String? = null,
    val proof: OwnershipProofRequest? = null
)
