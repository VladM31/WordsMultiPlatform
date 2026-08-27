package vm.words.ua.playlist.net.clients.impls

import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

