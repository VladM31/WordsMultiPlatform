package vm.words.ua.playlist.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class AssignPlayListsRequest(
    val playListIds: Set<String>
)
