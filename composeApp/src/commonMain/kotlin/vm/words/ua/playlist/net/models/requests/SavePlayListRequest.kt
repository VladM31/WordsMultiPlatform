package vm.words.ua.playlist.net.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SavePlayListRequest(
    val name: String
)
