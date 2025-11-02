package vm.words.ua.words.net.clients.impls

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import vm.words.ua.core.config.AppRemoteConfig
import vm.words.ua.core.net.responds.PagedRespond
import vm.words.ua.words.net.clients.WordClient
import vm.words.ua.words.net.responds.WordRespond

class KrotWordClient(
    private val client: HttpClient
) : WordClient {
    private val baseUrl by lazy {
        AppRemoteConfig.baseUrl + "/words-api/words"
    }

    override suspend fun findBy(
        token: String,
        filter: Map<String, String>
    ): PagedRespond<WordRespond> {
        return client.get(baseUrl) {
            header("Authorization", "Bearer $token")
            filter.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }
}