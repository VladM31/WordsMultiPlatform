package vm.words.ua.core.domain.managers.impl

import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import vm.words.ua.core.domain.crypto.TokenCipher
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token
import vm.words.ua.core.domain.models.User

/**
 * Multiplatform implementation of UserCacheManager using multiplatform-settings
 */
class SharedUserCacheManager(
    private val settings: Settings,
    private val tokenCipher: TokenCipher
) : UserCacheManager {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override val user: User
        get() = settings.getStringOrNull(USER_KEY)?.let {
            json.decodeFromString<User>(it)
        } ?: throw IllegalStateException("User not found")

    override val token: Token
        get() {
            val token = settings.getStringOrNull(TOKEN_KEY)?.let {
                json.decodeFromString<Token>(it)
            } ?: Token.Companion.DEFAULT

            val decryptedValue = tokenCipher.decrypt(token.value)

            return token.copy(value = decryptedValue)
        }

    override val isExpired: Boolean
        get() = settings.getStringOrNull(TOKEN_KEY)?.let {
            json.decodeFromString<Token>(it)
        }?.isExpired() ?: true

    override fun saveUser(user: User) {
        settings.putString(USER_KEY, json.encodeToString(user))
    }

    override fun saveToken(token: Token) {
        val encryptedValue = tokenCipher.encrypt(token.value)
        settings.putString(TOKEN_KEY, json.encodeToString(token.copy(value = encryptedValue)))
    }

    override suspend fun clear() {
        settings.remove(USER_KEY)
        settings.remove(TOKEN_KEY)
    }

    companion object {
        const val TOKEN_KEY = "TOKEN_KEY"
        const val USER_KEY = "USER_KEY"
    }
}