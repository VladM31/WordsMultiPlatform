package vm.words.ua.core.domain.managers.impl

import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import vm.words.ua.core.analytics.Analytics
import vm.words.ua.core.analytics.AnalyticsEvents
import vm.words.ua.core.domain.crypto.TokenCipher
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token
import vm.words.ua.core.domain.models.User

/**
 * Multiplatform implementation of UserCacheManager using multiplatform-settings
 */
class SharedUserCacheManager(
    private val settings: Settings,
    private val tokenCipher: TokenCipher,
    private val analytics: Analytics
) : UserCacheManager {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val mutableUserFlow = MutableStateFlow<User?>(null)

    private val mutableTokenFlow = MutableStateFlow<Token?>(null)
    override val tokenFlow = mutableTokenFlow.asStateFlow()

    init {
        settings.getStringOrNull(USER_KEY)?.let {
            try {
                val u = json.decodeFromString<User>(it)
                mutableUserFlow.value = u
            } catch (_: Throwable) {
            }
        }

        settings.getStringOrNull(TOKEN_KEY)?.let {
            try {
                val stored = json.decodeFromString<Token>(it)
                val decryptedValue = try {
                    tokenCipher.decrypt(stored.value)
                } catch (_: Throwable) {
                    ""
                }
                mutableTokenFlow.value = stored.copy(value = decryptedValue)
            } catch (_: Throwable) {
            }
        }
    }

    override val user: User
        get() = mutableUserFlow.value ?: throw IllegalStateException("User not found")

    override val token: Token
        get() = mutableTokenFlow.value ?: Token.DEFAULT

    override val isExpired: Boolean
        get() = mutableTokenFlow.value?.isExpired() != false

    override fun saveUser(user: User) {
        settings.putString(USER_KEY, json.encodeToString(user))
        analytics.setUserId(user.id)
        mutableUserFlow.value = user
    }

    override fun saveToken(token: Token) {
        val encryptedValue = tokenCipher.encrypt(token.value)
        settings.putString(TOKEN_KEY, json.encodeToString(token.copy(value = encryptedValue)))
        mutableTokenFlow.value = token
    }

    override fun clear() {
        settings.remove(USER_KEY)
        settings.remove(TOKEN_KEY)
        analytics.logEvent(
            AnalyticsEvents.LOGOUT, mapOf(
                "reason" to "user_initiated",
                "phone_number" to (mutableUserFlow.value?.phoneNumber ?: "unknown")
            )
        )
        mutableUserFlow.value = null
        mutableTokenFlow.value = null
    }

    companion object {
        const val TOKEN_KEY = "TOKEN_KEY"
        const val USER_KEY = "USER_KEY"
    }
}