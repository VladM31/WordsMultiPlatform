package vm.words.ua.playlist.net.clients.impls

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.playlist.net.clients.PinPlayListClient
import vm.words.ua.playlist.net.models.requests.PinPlayRequest

class KtorPinPlayListClient(
    private val client: HttpClient
) : PinPlayListClient {

    private val baseUrl: String = AppRemoteConfig.baseUrl

    override suspend fun pin(token: String, requests: List<PinPlayRequest>) {
        try {
            client.post("$baseUrl/words-api/pin") {
                header("Authorization", token)
                contentType(ContentType.Application.Json)
                setBody(requests)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun unpin(token: String, requests: List<PinPlayRequest>) {
        try {
            client.post("$baseUrl/words-api/unpin") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(requests)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

