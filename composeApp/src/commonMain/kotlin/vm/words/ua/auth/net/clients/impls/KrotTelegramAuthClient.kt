package vm.words.ua.auth.net.clients.impls

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import vm.words.ua.auth.net.clients.TelegramAuthClient
import vm.words.ua.auth.net.requests.TelegramAuthLoginReq
import vm.words.ua.auth.net.requests.TelegramAuthStartLoginReq
import vm.words.ua.auth.net.responses.TelegramLoginRespond
import vm.words.ua.core.config.AppRemoteConfig

class KrotTelegramAuthClient(
    private val httpClient: HttpClient
) : TelegramAuthClient {

    private val baseUrl by lazy {
        "${AppRemoteConfig.baseUrl}/auth"
    }

    override suspend fun startLogin(request: TelegramAuthStartLoginReq): String {
        val respond = httpClient.post("${baseUrl}/telegram-start-login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return respond.bodyAsText()
    }

    override suspend fun login(request: TelegramAuthLoginReq): TelegramLoginRespond {
        val respond = httpClient.post("${baseUrl}/telegram-login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        return respond.body()
    }
}