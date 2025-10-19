package vm.words.ua.playlist.net.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class PlayListCountRespond(
    val id: String,
    val userId: String,
    val name: String,
    val createdAt: String,
    val count: Long
)