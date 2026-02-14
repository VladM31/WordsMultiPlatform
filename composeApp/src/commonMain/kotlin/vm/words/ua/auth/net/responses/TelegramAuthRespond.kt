package vm.words.ua.auth.net.responses

data class TelegramAuthRespond(
    val isNotFound: Boolean,
    val code: String?,
)
