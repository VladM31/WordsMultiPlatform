package vm.words.ua.auth.net.requests

import kotlinx.serialization.Serializable

/** `PUT /user`: имя и фамилия, каждое от 2 до 60 символов. */
@Serializable
data class EditUserRequest(
    val firstName: String,
    val lastName: String
)
