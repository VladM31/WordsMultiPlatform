package vm.words.ua.auth.domain.managers.impls

import vm.words.ua.auth.domain.managers.UserManager
import vm.words.ua.auth.domain.models.DeleteOptions
import vm.words.ua.auth.net.clients.UserClient
import vm.words.ua.auth.net.requests.DeleteAccountRequest
import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.utils.toPair

class UserManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val userClient: UserClient
) : UserManager {
    override suspend fun delete(options: DeleteOptions): Boolean {
        val req = DeleteAccountRequest(
            password = options.password,
            reason = options.reason
        )
        val headerItem = userCacheManager.toPair()
        return userClient.deleteAccount(headerItem, req)
    }
}