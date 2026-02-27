package vm.words.ua.words.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.words.net.clients.UserWordClient
import vm.words.ua.words.net.requests.DeleteUserWordRequest
import vm.words.ua.words.net.requests.PinUserWordRequest
import vm.words.ua.words.net.requests.UserWordEditRequest
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
            header("Authorization", token)
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
            header("Content-Type", "application/json")
            setBody(requests)
        }
    }

    override suspend fun update(
        token: String,
        request: UserWordEditRequest
    ) {
        client.put(baseUrl) {
            header("Authorization", "Bearer $token")
            header("Content-Type", "application/json")
            setBody(request)
        }
    }

    override suspend fun pin(
        token: String,
        requests: List<PinUserWordRequest>
    ): List<UserWordRespond> {
        return client.post("$baseUrl/pin") {
            header("Authorization", "Bearer $token")
            header("Content-Type", "application/json")
            setBody(requests)
        }.body()
    }

    override suspend fun delete(
        token: String,
        requests: List<DeleteUserWordRequest>
    ) {
        client.post("$baseUrl/delete") {
            header("Authorization", "Bearer $token")
            header("Content-Type", "application/json")
            setBody(requests)
        }
    }
}