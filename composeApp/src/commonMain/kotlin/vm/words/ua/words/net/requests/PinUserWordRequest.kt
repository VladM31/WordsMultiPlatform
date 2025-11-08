package vm.words.ua.words.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class PinUserWordRequest(
    val wordId: String,
    val customSoundFileName: String?,
    val customImageFileName: String?,
)
