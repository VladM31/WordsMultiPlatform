package vm.words.ua.playlist.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class SavePlayListRequest(
    val name: String
)
