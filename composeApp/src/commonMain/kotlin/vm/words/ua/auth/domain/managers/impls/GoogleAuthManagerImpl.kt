package vm.words.ua.auth.domain.managers.impls

import kotlinx.coroutines.CancellationException
import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.managers.GoogleAuthManager
import vm.words.ua.auth.domain.mappers.toUser
import vm.words.ua.auth.domain.models.OAuthResult
import vm.words.ua.auth.net.clients.GoogleAuthClient
import vm.words.ua.auth.net.requests.google.GoogleSignInRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token

class GoogleAuthManagerImpl(
    private val googleAuthClient: GoogleAuthClient,
    private val googleApiManager: GoogleApiManager,
    private val userCacheManager: UserCacheManager
) : GoogleAuthManager {

    override suspend fun login(): OAuthResult {
        val idToken = try {
            val googleInfo = googleApiManager.signIn()
            if (googleInfo.success.not()) {
                return OAuthResult(false, googleInfo.errorMessage ?: "Google sign-in failed")
            }
            googleInfo.idToken ?: throw IllegalStateException("Google token is null")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            return OAuthResult(false, e.message)
        }

        return try {
            val response = googleAuthClient.signIn(GoogleSignInRequest(idToken))
            saveSession(response)
            OAuthResult(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            // ApiException несёт текст бэкенда: "Account is not active", 429 с временем ожидания и т.п.
            OAuthResult(false, e.message ?: "Google sign-in failed")
        }
    }

    private fun saveSession(response: AuthResponse) {
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
    }
}
