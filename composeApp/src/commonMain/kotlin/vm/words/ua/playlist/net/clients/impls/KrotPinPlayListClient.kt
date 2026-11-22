package vm.words.ua.playlist.net.clients.impls

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.playlist.net.clients.PinPlayListClient
import vm.words.ua.playlist.net.requests.pin.PinPlayListRequest
import vm.words.ua.playlist.net.requests.pin.UnPinPlayListRequest

class KrotPinPlayListClient(
    private val client: HttpClient
) : PinPlayListClient {
    private val baseUrl: String by lazy {
        AppRemoteConfig.baseUrl + "/words-api/play-list/"
    }

    override suspend fun pin(
        token: String,
        req: PinPlayListRequest
    ) {
        execute(token, req.playListId + "/pin")
    }

    override suspend fun unPin(
        token: String,
        req: UnPinPlayListRequest
    ) {
        execute(token, req.playListId + "/unpin")
    }

    private suspend fun execute(token: String, ending: String) {
        val url = baseUrl + ending
        client.patch(url) {
            header("Authorization", "Bearer $token")
            contentType(ContentType.Application.Json)
        }

    }
}