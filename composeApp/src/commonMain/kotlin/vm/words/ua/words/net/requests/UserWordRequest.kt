package vm.words.ua.words.net.requests

data class UserWordRequest(
    val customSoundFileName: String?,
    val customImageFileName: String?,
    val word: WordRequest
)