package vm.words.ua.playlist.net.clients

import vm.words.ua.playlist.net.requests.pin.PinPlayListRequest
import vm.words.ua.playlist.net.requests.pin.UnPinPlayListRequest

interface PinPlayListClient {

    suspend fun pin(token: String, req: PinPlayListRequest)

    suspend fun unPin(token: String, req: UnPinPlayListRequest)
}