package vm.words.ua.auth.domain.managers

import vm.words.ua.auth.domain.models.TelegramAuthResult
import vm.words.ua.auth.domain.models.TelegramAuthSession
import vm.words.ua.auth.domain.models.enums.TelegramMiniAppLoginStatus

interface TelegramAuthManager {

    suspend fun startLogin(phoneNumber: String): TelegramAuthResult

    suspend fun login(phoneNumber: String, code: String): Boolean

    suspend fun loginWithMiniApp(initData: String): TelegramMiniAppLoginStatus

    val session: TelegramAuthSession?

}