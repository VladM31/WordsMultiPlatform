package vm.words.ua.playlist.net.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlayListRequest(
    val id: String,
    val name: String
)