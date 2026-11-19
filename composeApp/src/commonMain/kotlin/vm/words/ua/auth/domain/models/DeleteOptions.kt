package vm.words.ua.auth.domain.models

data class DeleteOptions(
    /** Текущий пароль, если он задан */
    val password: String? = null,
    /** Подтверждение владения вместо пароля (для аккаунта без пароля или с забытым) */
    val proof: OwnershipProof? = null,
    val reason: String? = null,
)
