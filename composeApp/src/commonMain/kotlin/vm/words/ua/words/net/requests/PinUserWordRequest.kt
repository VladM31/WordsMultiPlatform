package vm.words.ua.words.net.requests

data class PinUserWordRequest(
    val wordId: String,
    val customSoundFileName: String?,
    val customImageFileName: String?,
)
