package vm.words.ua.subscribes.net.clients.impl

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import vm.words.ua.subscribes.net.clients.SubscribeClient
import vm.words.ua.subscribes.net.responds.SubscribeRespond
import vm.words.ua.core.config.AppRemoteConfig


class KtorSubscribeClient(
    private val client: HttpClient
) : SubscribeClient {

    override suspend fun fetch(): SubscribeRespond? {
        return try {
            val response: HttpResponse = client.get("${AppRemoteConfig.baseUrl}/pay/subscribe") {
                contentType(ContentType.Application.Json)
            }

            if (response.status.isSuccess()) {
                response.body<SubscribeRespond>()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}

