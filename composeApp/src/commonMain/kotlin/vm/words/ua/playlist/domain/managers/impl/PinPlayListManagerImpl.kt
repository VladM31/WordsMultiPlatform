package vm.words.ua.playlist.domain.managers.impl

import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.playlist.domain.models.PinPlayList
import vm.words.ua.playlist.domain.managers.PinPlayListManager
import vm.words.ua.playlist.net.clients.PinPlayListClient
import vm.words.ua.playlist.net.models.requests.PinPlayRequest

class PinPlayListManagerImpl(
    private val pinPlayListClient: PinPlayListClient,
    private val userCacheManager: UserCacheManager
) : PinPlayListManager {

    override suspend fun pin(requests: List<PinPlayList>) {
        try {
            pinPlayListClient.pin(
                userCacheManager.token.value,
                requests.map { PinPlayRequest(it.playListId, it.wordId) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun unpin(requests: List<PinPlayList>) {
        try {
            pinPlayListClient.unpin(
                userCacheManager.token.value,
                requests.map { PinPlayRequest(it.playListId, it.wordId) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

