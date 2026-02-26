package vm.words.ua.auth.net.responses.telegram

data class TelegramAuthRespond(
    val isNotFound: Boolean,
    val code: String?,
)