package vm.words.ua.playlist.net.clients.impls

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.http.ContentType
import io.ktor.http.contentType
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
            header("Authorization", token)
            contentType(ContentType.Application.Json)
        }

    }
}