package vm.words.ua.auth.domain.models

data class TelegramAuthResult(
    val code: String? = null,
    val isNotFound: Boolean = false
)