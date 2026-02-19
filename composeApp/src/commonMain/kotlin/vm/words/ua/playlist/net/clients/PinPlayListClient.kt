package vm.words.ua.playlist.net.clients

import vm.words.ua.playlist.net.requests.PinPlayRequest

interface PinPlayListClient {
    suspend fun pin(token: String, requests: List<PinPlayRequest>)
    suspend fun unpin(token: String, requests: List<PinPlayRequest>)
}

