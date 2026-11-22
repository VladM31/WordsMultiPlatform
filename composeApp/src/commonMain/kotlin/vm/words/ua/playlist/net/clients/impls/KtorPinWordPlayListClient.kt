package vm.words.ua.playlist.net.clients.impls

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.playlist.net.clients.PinWordPlayListClient
import vm.words.ua.playlist.net.requests.PinPlayRequest

class KtorPinWordPlayListClient(
    private val client: HttpClient
) : PinWordPlayListClient {

    private val baseUrl: String by lazy {
        AppRemoteConfig.baseUrl + "/words-api"
    }

    override suspend fun pin(token: String, requests: List<PinPlayRequest>) {
        try {
            client.post("$baseUrl/pin") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(requests)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun unpin(token: String, requests: List<PinPlayRequest>) {
        try {
            client.post("$baseUrl/unpin") {
                header("Authorization", "Bearer $token")
                contentType(ContentType.Application.Json)
                setBody(requests)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

