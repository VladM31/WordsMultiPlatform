package vm.words.ua.words.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUserWordRequest(
    val id: String,
    val wordId: String
)
