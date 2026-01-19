package vm.words.ua.auth.domain.models

data class TelegramAuthSession(
    val phoneNumber: String,
    val code: String
)
