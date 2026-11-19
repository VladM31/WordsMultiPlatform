package vm.words.ua.auth.domain.managers.impls

import vm.words.ua.auth.domain.managers.AuthManager
import vm.words.ua.auth.domain.mappers.toUser
import vm.words.ua.auth.domain.models.AuthResult
import vm.words.ua.auth.net.clients.AuthClient
import vm.words.ua.auth.net.requests.LoginRequest
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token

class AuthManagerImpl(
    private val authClient: AuthClient,
    private val  userCacheManager : UserCacheManager
) : AuthManager {
    override suspend fun logIn(
        login: String,
        password: String
    ): AuthResult {
        return try {
            val response = authClient.logIn(
                LoginRequest(
                    login = login.trim(),
                    password = password
                )
            )
            response.user?.let {
                userCacheManager.saveUser(it.toUser())
            }
            response.token?.let {
                userCacheManager.saveToken(
                    Token(
                        it.value,
                        it.expirationTime
                    )
                )
            }


            AuthResult(true)
        }catch (e: Exception) {
            AuthResult(false, e.message)
        }
    }

    override suspend fun isRegistered(phoneNumber: String): Boolean {
        return authClient.runCatching {
            isRegistered(phoneNumber)
        }.getOrDefault(false)
    }

    override suspend fun parseToken(): Boolean {
        return authClient.runCatching {
            parseToken(userCacheManager.token.value).let {
                userCacheManager.saveUser(it.toUser())
            }
            true
        }.getOrDefault(false)
    }
}
