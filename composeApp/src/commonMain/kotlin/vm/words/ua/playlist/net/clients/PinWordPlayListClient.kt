package vm.words.ua.playlist.net.clients

import vm.words.ua.playlist.net.requests.PinPlayRequest

interface PinWordPlayListClient {
    suspend fun pin(token: String, requests: List<PinPlayRequest>)
    suspend fun unpin(token: String, requests: List<PinPlayRequest>)
}

