package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.TelegramAuthSession

interface TelegramAuthManager {

    suspend fun startLogin(phoneNumber: String): String

    suspend fun login(phoneNumber: String, code: String): Boolean

    val session: TelegramAuthSession?

}