package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.TelegramAuthResult
import vm.words.ua.auth.domain.models.TelegramAuthSession

interface TelegramAuthManager {

    suspend fun startLogin(phoneNumber: String): TelegramAuthResult

    suspend fun login(phoneNumber: String, code: String): Boolean

    val session: TelegramAuthSession?

}