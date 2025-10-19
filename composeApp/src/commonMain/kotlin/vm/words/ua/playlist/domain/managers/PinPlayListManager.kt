package vm.words.ua.playlist.domain.managers

import vm.words.ua.playlist.domain.models.PinPlayList

interface PinPlayListManager {
    suspend fun pin(requests: List<PinPlayList>)
    suspend fun unpin(requests: List<PinPlayList>)
}

