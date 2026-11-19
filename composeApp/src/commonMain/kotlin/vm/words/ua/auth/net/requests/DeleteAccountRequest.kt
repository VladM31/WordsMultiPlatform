package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

/**
 * `POST /user/delete`. Нужно подтвердить владение: [password] (если он задан)
 * или [proof] (единственный способ для аккаунта без пароля или с забытым паролем).
 */
@Serializable
data class DeleteAccountRequest(
    val password: String? = null,
    val proof: OwnershipProofRequest? = null,
    val reason: String? = null
)
