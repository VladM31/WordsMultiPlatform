package vm.words.ua.playlist.domain.usecases

import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.core.domain.models.ExecuteResult
import vm.words.ua.playlist.net.clients.PinPlayListClient
import vm.words.ua.playlist.net.requests.pin.PinPlayListRequest

class PinPlayListUseCase(
    private val userCacheManager: UserCacheManager,
    private val pinPlayListClient: PinPlayListClient
) {

    suspend fun execute(playListId: String): ExecuteResult {
        return try {
            pinPlayListClient.pin(userCacheManager.token.value, PinPlayListRequest(playListId))
            ExecuteResult.success()
        } catch (e: Exception) {
            ExecuteResult.failure(e.message.orEmpty())
        }
    }
}