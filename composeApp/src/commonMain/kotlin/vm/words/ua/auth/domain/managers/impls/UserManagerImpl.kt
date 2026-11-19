package vm.words.ua.auth.domain.managers.impls

import vm.words.ua.auth.domain.managers.GoogleApiManager
import vm.words.ua.auth.domain.managers.TelegramAuthManager
import vm.words.ua.auth.domain.managers.UserManager
import vm.words.ua.auth.domain.mappers.toUser
import vm.words.ua.auth.domain.models.DeleteOptions
import vm.words.ua.auth.domain.models.OwnershipProof
import vm.words.ua.auth.domain.models.OwnershipPurpose
import vm.words.ua.auth.domain.models.enums.TelegramCodeStatus
import vm.words.ua.auth.net.clients.UserClient
import vm.words.ua.auth.net.requests.DeleteAccountRequest
import vm.words.ua.auth.net.requests.EditUserRequest
import vm.words.ua.auth.net.requests.OwnershipProofRequest
import vm.words.ua.auth.net.requests.SetPasswordRequest
import vm.words.ua.auth.net.requests.google.GoogleSignInRequest
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.Token
import vm.words.ua.core.utils.toPair

class UserManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val userClient: UserClient,
    private val googleApiManager: GoogleApiManager,
    private val telegramAuthManager: TelegramAuthManager
) : UserManager {

    override val isGoogleAvailable: Boolean
        get() = googleApiManager.isAvailable()

    override suspend fun delete(options: DeleteOptions) {
        val req = DeleteAccountRequest(
            password = options.password?.takeIf { it.isNotEmpty() },
            proof = options.proof?.toRequest(),
            reason = options.reason
        )
        userClient.deleteAccount(userCacheManager.toPair(), req)
    }

    override suspend fun editName(firstName: String, lastName: String) {
        val response = userClient.editUser(
            userCacheManager.toPair(),
            EditUserRequest(firstName = firstName.trim(), lastName = lastName.trim())
        )
        // В ответе новый токен: старый остаётся валидным, но сохраняем актуальный
        response.user?.let { userCacheManager.saveUser(it.toUser()) }
        response.token?.let { userCacheManager.saveToken(Token(it.value, it.expirationTime)) }
    }

    override suspend fun setPassword(newPassword: String, oldPassword: String?, proof: OwnershipProof?) {
        userClient.setPassword(
            userCacheManager.toPair(),
            SetPasswordRequest(
                newPassword = newPassword,
                oldPassword = oldPassword?.takeIf { it.isNotEmpty() },
                proof = proof?.toRequest()
            )
        )
        userCacheManager.saveUser(userCacheManager.user.copy(hasPassword = true))
    }

    override suspend fun linkGoogle() {
        val googleInfo = googleApiManager.signIn()
        if (googleInfo.success.not()) {
            throw IllegalStateException(googleInfo.errorMessage ?: "Google sign-in failed")
        }
        val idToken = googleInfo.idToken ?: throw IllegalStateException("Google token is null")
        val user = userClient.linkGoogle(userCacheManager.toPair(), GoogleSignInRequest(idToken))
        userCacheManager.saveUser(user.toUser())
    }

    override suspend fun requestGoogleProof(): OwnershipProof {
        val googleInfo = googleApiManager.signIn()
        if (googleInfo.success.not()) {
            throw IllegalStateException(googleInfo.errorMessage ?: "Google sign-in failed")
        }
        return OwnershipProof.google(
            googleInfo.idToken ?: throw IllegalStateException("Google token is null")
        )
    }

    override suspend fun requestTelegramProofCode(purpose: OwnershipPurpose): String {
        val phoneNumber = userCacheManager.user.phoneNumber
        if (phoneNumber.isNullOrBlank()) {
            throw IllegalStateException("Your account has no phone number, confirm with Google instead")
        }
        val result = telegramAuthManager.startProof(phoneNumber, purpose)
        if (result.isNotFound) {
            throw IllegalStateException("The Telegram bot doesn't know your phone number. Open the bot and send your contact first")
        }
        return result.code ?: throw IllegalStateException("Telegram code is empty")
    }

    override suspend fun telegramProofStatus(code: String): TelegramCodeStatus {
        return telegramAuthManager.codeStatus(code)
    }

    private fun OwnershipProof.toRequest() = OwnershipProofRequest(
        provider = provider.value,
        idToken = idToken,
        code = code,
        initData = initData
    )
}
