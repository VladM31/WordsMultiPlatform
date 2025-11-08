package vm.words.ua.words.net.clients.impls

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.words.net.clients.UserWordClient
import vm.words.ua.words.net.requests.DeleteUserWordRequest
import vm.words.ua.words.net.requests.PinUserWordRequest
import vm.words.ua.words.net.requests.UserWordRequest
import vm.words.ua.words.net.responds.UserWordRespond

class KrotUserWordClient(
    private val client: HttpClient
) : UserWordClient {

    private val baseUrl by lazy {
        AppRemoteConfig.baseUrl + "/words-api/user-words"
    }


    override suspend fun findBy(
        token: String,
        filter: Map<String, String>
    ): PagedRespond<UserWordRespond> {
        return client.get(baseUrl) {
            header("Authorization", "Bearer $token")
            filter.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }

    override suspend fun save(
        token: String,
        requests: List<UserWordRequest>
    ) {
        client.post(baseUrl) {
            header("Authorization", "Bearer $token")
            setBody(requests)
        }
    }

    override suspend fun pin(
        token: String,
        requests: List<PinUserWordRequest>
    ) {
        client.post("$baseUrl/pin") {
            header("Authorization", "Bearer $token")
            setBody(requests)
        }
    }

    override suspend fun delete(
        token: String,
        requests: List<DeleteUserWordRequest>
    ) {
        client.post("$baseUrl/delete") {
            header("Authorization", "Bearer $token")
            setBody(requests)
        }
    }
}