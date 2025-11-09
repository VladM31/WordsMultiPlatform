package vm.words.ua.auth.domain.managers.impls

import vm.words.ua.auth.domain.managers.TelegramAuthManager
import vm.words.ua.auth.domain.mappers.toUser
import vm.words.ua.auth.net.clients.TelegramAuthClient
import vm.words.ua.auth.net.requests.TelegramAuthLoginReq
import vm.words.ua.auth.net.requests.TelegramAuthStartLoginReq
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token
import vm.words.ua.core.platform.getDeviceName

class TelegramAuthManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val telegramAuthClient: TelegramAuthClient
) : TelegramAuthManager {
    override suspend fun startLogin(phoneNumber: String): String {
        return telegramAuthClient.startLogin(
            TelegramAuthStartLoginReq(
                phoneNumber,
                getDeviceName()
            )
        )
    }

    override suspend fun login(phoneNumber: String, code: String): Boolean {
        val respond = telegramAuthClient.login(
            TelegramAuthLoginReq(
                phoneNumber,
                code
            )
        )

        if (respond.success.not()) {
            return false
        }


        userCacheManager.saveUser(respond.toUser())
        userCacheManager.saveToken(
            Token(
                respond.token?.value ?: "",
                respond.token?.expirationTime ?: 0
            )
        )

        return true
    }
}