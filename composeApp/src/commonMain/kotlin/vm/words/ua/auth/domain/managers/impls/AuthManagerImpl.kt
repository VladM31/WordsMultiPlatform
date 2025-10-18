package vm.words.ua.auth.domain.managers.impls

import vm.words.ua.auth.domain.managers.AuthManager
import vm.words.ua.auth.domain.mappers.toRequest
import vm.words.ua.auth.domain.mappers.toUser
import vm.words.ua.auth.domain.models.data.AuthResult
import vm.words.ua.auth.domain.models.data.SignUpModel
import vm.words.ua.auth.net.clients.AuthClient
import vm.words.ua.auth.net.requests.LoginRequest
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token

class AuthManagerImpl(
    private val authClient: AuthClient,
    private val  userCacheManager : UserCacheManager
) : AuthManager {
    override suspend fun logIn(
        phoneNumber: String,
        password: String
    ): AuthResult {
        return try {
            val response = authClient.logIn(
                LoginRequest(
                    phoneNumber,
                    password
                )
            )
            if (response.hasError()) {
                return AuthResult(false, response.error?.message)
            }
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

    override suspend fun signUp(req: SignUpModel): AuthResult {
        return authClient.runCatching {
            signUp(req.toRequest()).let {
                AuthResult(it.success, it.error)
            }
        }.getOrDefault(AuthResult(false))
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