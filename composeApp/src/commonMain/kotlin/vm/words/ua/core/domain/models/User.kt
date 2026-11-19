package vm.words.ua.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val phoneNumber: String?,
    val firstName: String,
    val lastName: String,
    val email: String?,
    val role: String,
    val dateOfLonIn: Long,
    /** Задан ли пароль, `null` пока неизвестно (старые данные в кэше) */
    val hasPassword: Boolean? = null
)
