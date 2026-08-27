package vm.words.ua.playlist.domain.managers.impl

import vm.words.ua.core.domain.managers.UserCacheManager
import vm.words.ua.playlist.domain.managers.PinWordPlayListManager
import vm.words.ua.playlist.domain.models.PinPlayList
import vm.words.ua.playlist.net.clients.PinWordPlayListClient
import vm.words.ua.playlist.net.requests.PinPlayRequest

class PinWordWordPlayListManagerImpl(
    private val pinWordPlayListClient: PinWordPlayListClient,
    private val userCacheManager: UserCacheManager
) : PinWordPlayListManager {

    override suspend fun pin(requests: List<PinPlayList>) {
        try {
            pinWordPlayListClient.pin(
                userCacheManager.token.value,
                requests.map { PinPlayRequest(it.playListId, it.wordId) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun unpin(requests: List<PinPlayList>) {
        try {
            pinWordPlayListClient.unpin(
                userCacheManager.token.value,
                requests.map { PinPlayRequest(it.playListId, it.wordId) }
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

