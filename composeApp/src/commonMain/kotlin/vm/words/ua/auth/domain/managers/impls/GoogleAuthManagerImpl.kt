package vm.words.ua.auth.domain.managers.impls

import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.managers.GoogleAuthManager
import vm.words.ua.auth.domain.mappers.toUser
import vm.words.ua.auth.domain.models.AuthResult
import vm.words.ua.auth.domain.models.google.GmailLoginDto
import vm.words.ua.auth.domain.models.google.GoogleSingUpDto
import vm.words.ua.auth.net.clients.GoogleAuthClient
import vm.words.ua.auth.net.requests.google.GmailLoginRequest
import vm.words.ua.auth.net.requests.google.GoogleSingUpRequest
import vm.words.ua.auth.net.requests.google.GoogleTokenLoginRequest
import vm.words.ua.auth.net.responses.AuthResponse
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token

class GoogleAuthManagerImpl(
    private val googleAuthClient: GoogleAuthClient,
    private val googleApiManager: GoogleApiManager,
    private val userCacheManager: UserCacheManager
) : GoogleAuthManager {
    override suspend fun login(): AuthResult {
        return try {
            val googleInfo = googleApiManager.signIn()
            if (googleInfo.success.not()) {
                return AuthResult(false, googleInfo.errorMessage ?: "Google sign-in failed")
            }
            val req = GoogleTokenLoginRequest(googleInfo.idToken ?: throw IllegalStateException("Google token is null"))
            val response = googleAuthClient.loginWithGoogleToken(req)
            toResult(response)
        } catch (e: Exception) {
            AuthResult(false, e.message)
        }

    }

    override suspend fun login(dto: GmailLoginDto): AuthResult {
        return try {
            val req = GmailLoginRequest(
                email = dto.email,
                password = dto.password
            )
            val response = googleAuthClient.login(req)
            toResult(response)
        } catch (e: Exception) {
            AuthResult(false, e.message)
        }
    }

    override suspend fun signUp(dto: GoogleSingUpDto): AuthResult {
        return try {
            val googleInfo = googleApiManager.signIn()
            if (googleInfo.success.not()) {
                return AuthResult(false, googleInfo.errorMessage ?: "Google sign-in failed")
            }
            val req = GoogleSingUpRequest(
                idToken = googleInfo.idToken ?: throw IllegalStateException("Google token is null"),
                password = dto.password,
                firstName = dto.firstName,
                lastName = dto.lastName,
                currency = dto.currency
            )
            val response = googleAuthClient.signUp(req)
            toResult(response)
        } catch (e: Exception) {
            AuthResult(false, e.message)
        }
    }

    private fun toResult(response: AuthResponse): AuthResult {
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

        return AuthResult(true)
    }
}