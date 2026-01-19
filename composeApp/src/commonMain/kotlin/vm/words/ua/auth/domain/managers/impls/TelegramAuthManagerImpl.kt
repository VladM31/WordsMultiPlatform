package vm.words.ua.auth.domain.managers.impls

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus
import vm.words.ua.auth.domain.managers.TelegramAuthManager
import vm.words.ua.auth.domain.mappers.toUser
import vm.words.ua.auth.domain.models.TelegramAuthSession
import vm.words.ua.auth.net.clients.TelegramAuthClient
import vm.words.ua.auth.net.requests.TelegramAuthLoginReq
import vm.words.ua.auth.net.requests.TelegramAuthStartLoginReq
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token
import vm.words.ua.core.platform.getDeviceName
import vm.words.ua.utils.storage.AppStorage
import vm.words.ua.utils.storage.managers.KeyValueStorage

class TelegramAuthManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val telegramAuthClient: TelegramAuthClient
) : TelegramAuthManager {
    private val storageManager: KeyValueStorage = AppStorage.create("TelegramAuthManagerImpl")

    override suspend fun startLogin(phoneNumber: String): String {
        val code = telegramAuthClient.startLogin(
            TelegramAuthStartLoginReq(
                phoneNumber,
                getDeviceName()
            )
        )

        val expireDate = Clock.System.now().plus(10, DateTimeUnit.MINUTE)

        storageManager.putString(CODE_KEY, code)
        storageManager.putString(PHONE_NUMBER_KEY, phoneNumber)
        storageManager.putString(EXPIRY_DATE_KEY, expireDate.toEpochMilliseconds().toString())

        return code
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
        storageManager.remove(CODE_KEY)
        storageManager.remove(PHONE_NUMBER_KEY)
        storageManager.remove(EXPIRY_DATE_KEY)

        return true
    }

    override val session: TelegramAuthSession?
        get() {
            val expireDate = storageManager.getString(EXPIRY_DATE_KEY, null)?.toLongOrNull()?.let {
                Instant.fromEpochMilliseconds(it)
            } ?: return null
            val now = Clock.System.now()
            if (now >= expireDate) {
                return null
            }
            return TelegramAuthSession(
                phoneNumber = storageManager.getString(PHONE_NUMBER_KEY, null).orEmpty(),
                code = storageManager.getString(CODE_KEY, null).orEmpty(),
            )
        }

    companion object {
        private const val CODE_KEY = "code"
        private const val EXPIRY_DATE_KEY = "expiry_date"
        private const val PHONE_NUMBER_KEY = "phone_number"
    }
}