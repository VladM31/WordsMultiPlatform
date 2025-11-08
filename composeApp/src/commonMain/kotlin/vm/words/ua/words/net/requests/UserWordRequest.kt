package vm.words.ua.words.net.requests

import kotlinx.serialization.Serializable

@Serializable
data class UserWordRequest(
    val customSoundFileName: String?,
    val customImageFileName: String?,
    val word: WordRequest
)