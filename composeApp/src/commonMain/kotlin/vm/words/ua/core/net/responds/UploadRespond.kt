package vm.words.ua.core.net.responds

import kotlinx.serialization.Serializable

@Serializable
data class UploadRespond(
    val fileName: String
)
