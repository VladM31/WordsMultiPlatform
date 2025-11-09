package vm.words.ua.auth.domain.managers

interface TelegramAuthManager {

    suspend fun startLogin(phoneNumber: String): String

    suspend fun login(phoneNumber: String, code: String): Boolean
}