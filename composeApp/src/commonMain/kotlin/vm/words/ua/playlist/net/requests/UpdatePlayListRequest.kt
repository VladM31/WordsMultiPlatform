package vm.words.ua.playlist.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePlayListRequest(
    val id: String,
    val name: String
)