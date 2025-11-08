package vm.words.ua.core.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class SaveFileRequest(
    val content: ByteArray,
    val fileName: String,
)
