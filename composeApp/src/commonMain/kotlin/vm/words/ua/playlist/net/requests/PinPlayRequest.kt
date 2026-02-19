package vm.words.ua.playlist.net.requests

import kotlinx.serialization.Serializable


@Serializable
data class PinPlayRequest(
    val playListId: String,
    val wordId: String
)

